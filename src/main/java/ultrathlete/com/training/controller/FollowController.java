package ultrathlete.com.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jenetics.jpx.GPX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.impl.RunnerPosition;
import ultrathlete.com.training.data.PredictedSessionDAO;
import ultrathlete.com.training.data.RunnerPositionDAO;
import ultrathlete.com.training.model.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin
public class FollowController {

    @Autowired
    private PredictedSessionDAO predictedSessionDAO;
    @Autowired
    private RunnerPositionDAO runnerPositionDAO;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ── Runner broadcasts position ─────────────────────────────────
    @PostMapping("/{sessionId}/position")
    public ResponseEntity<Void> updatePosition(
            @PathVariable int sessionId,
            @RequestBody PositionBroadcast broadcast) {

        // Try to calculate distanceFromStartM from GPS position
        // using the stored MobileSession route
        PredictedSession session = predictedSessionDAO
                .findById(sessionId).orElse(null);

        double distanceFromStart = broadcast.getDistanceFromStartM();

        if (session != null && session.getSessionData() != null) {
            try {
                MobileSession mobile = objectMapper.readValue(
                        session.getSessionData(), MobileSession.class);
                double calculated = calculateDistanceFromStart(
                        broadcast.getLatitude(),
                        broadcast.getLongitude(),
                        mobile);
                if (calculated >= 0) {
                    distanceFromStart = calculated;
                }
            } catch (Exception e) {
                // Fall back to broadcast value
            }
        }

        RunnerPosition pos = runnerPositionDAO
                .findById(sessionId)
                .orElse(new RunnerPosition());

        pos.setSessionId(sessionId);
        pos.setLatitude(broadcast.getLatitude());
        pos.setLongitude(broadcast.getLongitude());
        pos.setAltitude(broadcast.getAltitude());
        pos.setDistanceFromStartM(distanceFromStart);
        pos.setElapsedSeconds(broadcast.getElapsedSeconds());
        pos.setLastUpdate(new Date());
        runnerPositionDAO.save(pos);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Find the cumulative distance from start to the nearest
     * GPS point on the route to the given position.
     * Returns -1 if no match found within 200m.
     */
    private double calculateDistanceFromStart(
            double lat, double lon, MobileSession mobile) {

        double bestDist    = Double.MAX_VALUE;
        double bestCumDist = -1;
        double cumDist     = 0;

        if (mobile.getSegments() == null) return -1;

        for (MobileSegment seg : mobile.getSegments()) {
            if (seg.getGpsPoints() == null) continue;

            double prevLat = 0, prevLon = 0;
            boolean first = true;

            for (MobileGpsPoint pt : seg.getGpsPoints()) {
                if (!first) {
                    cumDist += haversine(prevLat, prevLon,
                            pt.getLatitude(), pt.getLongitude());
                }
                first   = false;
                prevLat = pt.getLatitude();
                prevLon = pt.getLongitude();

                double d = haversine(lat, lon,
                        pt.getLatitude(), pt.getLongitude());

                if (d < bestDist) {
                    bestDist    = d;
                    bestCumDist = cumDist;
                }
            }
        }

        // Only trust the result if within 200m of the route
        return bestDist <= 200 ? bestCumDist : -1;
    }

    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {
        final double R = 6_371_000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }
    // ── Follower gets session info (route + name) ──────────────────

    @GetMapping("/{sessionId}/session")
    public ResponseEntity<FollowerSessionInfo> getSessionInfo(
            @PathVariable int sessionId) {

        PredictedSession session = predictedSessionDAO
                .findById(sessionId).orElse(null);

        if (session == null || session.getSessionData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            MobileSession mobile = objectMapper.readValue(
                    session.getSessionData(), MobileSession.class);

            FollowerSessionInfo info = new FollowerSessionInfo();
            info.setRaceName(mobile.getRaceName());
            info.setSessionId(sessionId);

            // ── Route polyline from MobileSession GPS points ───────────
            List<double[]> routePoints = new ArrayList<>();
            if (mobile.getSegments() != null) {
                for (MobileSegment seg : mobile.getSegments()) {
                    if (seg.getGpsPoints() != null) {
                        for (MobileGpsPoint pt : seg.getGpsPoints()) {
                            routePoints.add(new double[]{
                                    pt.getLatitude(),
                                    pt.getLongitude()
                            });
                        }
                    }
                }
            }
            info.setRoutePoints(routePoints);

            // ── Elevation profile from raw GPX ─────────────────────────
            // We re-parse the stored GPX because MobileGpsPoint altitude
            // may be 0 — the raw GPX always has elevation data
            if (session.getGpxData() != null) {
                GPX gpx = GPX.read(
                        new ByteArrayInputStream(session.getGpxData()));

                List<double[]> profile = new ArrayList<>();
                double cumDist = 0;
                double prevLat = 0, prevLon = 0;
                boolean first  = true;
                int count      = 0;

                for (io.jenetics.jpx.Track track : gpx.getTracks()) {
                    for (io.jenetics.jpx.TrackSegment seg
                            : track.getSegments()) {
                        for (io.jenetics.jpx.WayPoint wp
                                : seg.getPoints()) {
                            double lat = wp.getLatitude().doubleValue();
                            double lon = wp.getLongitude().doubleValue();
                            double alt = wp.getElevation()
                                    .map(e -> e.doubleValue()).orElse(0.0);

                            if (!first) {
                                cumDist += haversine(
                                        prevLat, prevLon, lat, lon);
                            }
                            first   = false;
                            prevLat = lat;
                            prevLon = lon;

                            // Sample every 5th point to keep response small
                            if (count % 5 == 0 && alt > 0) {
                                profile.add(new double[]{cumDist, alt});
                            }
                            count++;
                        }
                    }
                }

                info.setElevationProfile(profile);
                info.setTotalDistanceM(cumDist);

            } else {
                // No GPX stored — calculate distance from route points only
                double cumDist = 0;
                double prevLat = 0, prevLon = 0;
                boolean first  = true;
                for (double[] pt : routePoints) {
                    if (!first) {
                        cumDist += haversine(prevLat, prevLon,
                                pt[0], pt[1]);
                    }
                    first   = false;
                    prevLat = pt[0];
                    prevLon = pt[1];
                }
                info.setTotalDistanceM(cumDist);
                info.setElevationProfile(new ArrayList<>());
            }

            // ── Checkpoints ────────────────────────────────────────────
            List<FollowerCheckpoint> checkpoints = new ArrayList<>();
            if (mobile.getSegments() != null) {
                for (MobileSegment seg : mobile.getSegments()) {
                    FollowerCheckpoint cp = new FollowerCheckpoint();
                    cp.setName(seg.getName());
                    cp.setDistanceFromStartM(seg.getDistanceFromStartM());
                    cp.setPassed(false);
                    cp.setEtaTimestamp(null);
                    checkpoints.add(cp);
                }
            }
            info.setCheckpoints(checkpoints);

            return new ResponseEntity<>(info, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // ── Follower polls for live position + ETAs ────────────────────

    @GetMapping("/{sessionId}/position")
    public ResponseEntity<FollowerUpdate> getLivePosition(
            @PathVariable int sessionId) {

        RunnerPosition pos = runnerPositionDAO
                .findById(sessionId).orElse(null);

        PredictedSession session = predictedSessionDAO
                .findById(sessionId).orElse(null);

        if (session == null || session.getSessionData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        FollowerUpdate update = new FollowerUpdate();

        // No position yet — race not started
        if (pos == null) {
            update.setRaceStarted(false);
            return new ResponseEntity<>(update, HttpStatus.OK);
        }

        update.setRaceStarted(true);
        update.setLatitude(pos.getLatitude());
        update.setLongitude(pos.getLongitude());
        update.setDistanceFromStartM(pos.getDistanceFromStartM());
        update.setElapsedSeconds(pos.getElapsedSeconds());
        update.setLastUpdate(pos.getLastUpdate());

        // Calculate ETAs from current position
        try {
            MobileSession mobile = objectMapper.readValue(
                    session.getSessionData(), MobileSession.class);

            update.setCheckpointEtas(
                    calculateEtas(pos, mobile));

        } catch (Exception e) {
            // ETAs unavailable — still return position
        }

        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    // ── ETA calculation — same logic as mobile ─────────────────────
    private List<FollowerCheckpoint> calculateEtas(
            RunnerPosition pos, MobileSession mobile) {

        List<MobileSegment> segments = mobile.getSegments();
        List<FollowerCheckpoint> result = new ArrayList<>();

        if (segments == null || segments.isEmpty()) return result;

        double runnerDist = pos.getDistanceFromStartM();
        long now          = System.currentTimeMillis();

        // Find current segment index
        int currentSegIdx = segments.size() - 1;
        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).getDistanceFromStartM() >= runnerDist) {
                currentSegIdx = i;
                break;
            }
        }

        // ── Prorate the current segment ────────────────────────────
        // How far through the current segment is the runner?
        // segStart = distanceFromStartM of PREVIOUS segment (or 0)
        // segEnd   = distanceFromStartM of CURRENT segment
        MobileSegment currentSeg = segments.get(currentSegIdx);
        double segEnd   = currentSeg.getDistanceFromStartM();
        double segStart = currentSegIdx > 0
                ? segments.get(currentSegIdx - 1).getDistanceFromStartM()
                : 0;
        double segLength = segEnd - segStart;

        // Fraction of current segment already covered
        double fractionDone = segLength > 0
                ? Math.min(1.0, (runnerDist - segStart) / segLength)
                : 0;

        // Remaining time in current segment
        long currentSegTimeLeft = (long)
                (currentSeg.getRunningTimeModel2() * (1.0 - fractionDone));

        // ── Build ETA for each checkpoint ─────────────────────────
        for (int i = 0; i < segments.size(); i++) {
            MobileSegment seg = segments.get(i);
            FollowerCheckpoint cp = new FollowerCheckpoint();
            cp.setName(seg.getName());
            cp.setDistanceFromStartM(seg.getDistanceFromStartM());

            if (i < currentSegIdx) {
                // Already passed
                cp.setPassed(true);
                cp.setEtaTimestamp(null);
            } else if (i == currentSegIdx) {
                // Current segment — use prorated time
                cp.setPassed(false);
                cp.setEtaTimestamp(now + currentSegTimeLeft * 1000L);
            } else {
                // Future segments — add full segment times
                long secsToThis = currentSegTimeLeft;
                for (int j = currentSegIdx + 1; j <= i; j++) {
                    secsToThis += segments.get(j).getRunningTimeModel2();
                }
                cp.setPassed(false);
                cp.setEtaTimestamp(now + secsToThis * 1000L);
            }
            result.add(cp);
        }

        return result;
    }
}