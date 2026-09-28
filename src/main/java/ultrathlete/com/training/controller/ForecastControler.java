package ultrathlete.com.training.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.bo.impl.SessionFileImpl;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.data.PredictedSessionDAO;
import ultrathlete.com.training.data.UserDAO;
import ultrathlete.com.training.model.*;
import ultrathlete.com.training.service.UserService;
import ultrathlete.com.training.service.impl.ForecastManager;
import ultrathlete.com.training.service.impl.SegmentBuilder;
import ultrathlete.com.training.service.model.Models;
import ultrathlete.com.training.service.model.PredictionModel;
import ultrathlete.com.training.util.TimeUtil;
import io.jenetics.jpx.GPX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/forecast")
@CrossOrigin
public class ForecastControler {

    @Autowired
    private UserService userService;
    @Autowired
    private ForecastManager forecastManager;
    @Autowired
    private PredictedSessionDAO predictedSessionDAO;
    @Autowired
    private UserDAO userDAO;

    // ═══════════════════════════════════════════════════════════════
    // EXISTING ENDPOINTS
    // ═══════════════════════════════════════════════════════════════

    @GetMapping(path = "/")
    public ResponseEntity<Forecast> forecast(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") int userId,
            @RequestParam("name") String name,
            @RequestParam("points") Double[] points,
            @RequestParam("names") String[] names) throws IOException {

        User user = userService.getUser(userId);
        if (user != null) {
            List<PredictionModel> predictions =
                    forecastManager.predictRun(file.getInputStream(), (UserImpl) user);
            Forecast forecast = generateForecast(name, predictions, points, names);
            return new ResponseEntity<>(forecast, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Forecast(), HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(path = "/reference")
    public ResponseEntity<Models> addReference(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("userId") int userId) throws IOException {

        UserImpl user = (UserImpl) userService.getUser(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Convert MultipartFile[] to SessionFileImpl[]
        SessionFileImpl[] sessionFiles = new SessionFileImpl[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            SessionFileImpl sessionFile = new SessionFileImpl();
            sessionFile.setName(file.getOriginalFilename());
            sessionFile.setType(file.getContentType());
            sessionFile.setSize(file.getSize());
            sessionFile.setData(file.getBytes());
            sessionFiles[i] = sessionFile;
        }

        // Add reference data and rebuild pace model
        Models models = forecastManager.addReferenceData(sessionFiles, user);

        if (models == null) {
            return new ResponseEntity<>(new Models(), HttpStatus.OK);
        }

        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @PostMapping(path = "/predict")
    public ResponseEntity<List<SegmentForecast>> predict(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") int userId) throws IOException {

        UserImpl user = (UserImpl) userService.getUser(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.getModel2() == null && user.getModel3() == null) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }

        List<PredictionModel> predictions =
                forecastManager.predictRun(file.getInputStream(), user);

        if (predictions == null || predictions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<SegmentForecast> result = predictions.stream()
                .map(p -> buildSegmentForecast(
                        String.format("%.2f km", p.getRanSegment().getTotalDistance() / 1000), p))
                .collect(Collectors.toList());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // ═══════════════════════════════════════════════════════════════
    // NEW: ANALYSE GPX — saves GPX to PredictedSession, returns
    //      elevation profile + suggested checkpoints + sessionId
    // ═══════════════════════════════════════════════════════════════

    @PostMapping(path = "/analyse")
    public ResponseEntity<RaceAnalysisResponse> analyseGpx(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") int userId) throws IOException {

        byte[] bytes = file.getBytes();

        // ── Build segments at 100m resolution ─────────────────────
        SegmentBuilder segmentBuilder = new SegmentBuilder();
        List<Segment> segments = segmentBuilder.getSegments(
                new ByteArrayInputStream(bytes), 100);

        if (segments == null || segments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // ── Race summary ───────────────────────────────────────────
        double totalDistanceM = 0;
        int totalGain = 0;
        int totalLoss = 0;
        for (Segment seg : segments) {
            totalDistanceM += seg.getDistance();
            int elev = seg.getElevation(); // elevationTo - elevationFrom
            if (elev > 0) totalGain += elev;
            else totalLoss += Math.abs(elev);
        }

        RaceAnalysis analysis = new RaceAnalysis();
        analysis.setTotalDistanceM(totalDistanceM);
        analysis.setTotalElevationGain(totalGain);
        analysis.setTotalElevationLoss(totalLoss);

        // ── Elevation profile — every 5 segments (~500m) ──────────
        List<ElevationPoint> profile = new ArrayList<>();
        double cumDist = 0;
        for (int i = 0; i < segments.size(); i++) {
            Segment seg = segments.get(i);
            cumDist += seg.getDistance();
            if (i % 5 == 0 || i == segments.size() - 1) {
                ElevationPoint pt = new ElevationPoint();
                pt.setDistanceM(cumDist);
                pt.setElevationM(seg.getElevationTo() != null
                        ? seg.getElevationTo() : 0);
                profile.add(pt);
            }
        }
        analysis.setElevationProfile(profile);

        // ── Named waypoints from GPX ───────────────────────────────
        List<SuggestedCheckpoint> checkpoints = new ArrayList<>();
        GPX gpx = GPX.read(new ByteArrayInputStream(bytes));
        gpx.wayPoints().forEach(wp -> {
            SuggestedCheckpoint cp = new SuggestedCheckpoint();
            cp.setName(wp.getName().orElse("Waypoint"));
            cp.setType("waypoint");
            double lat = wp.getLatitude().doubleValue();
            double lon = wp.getLongitude().doubleValue();
            cp.setDistanceM(findClosestCumulativeDistance(segments, lat, lon));
            wp.getElevation().ifPresent(e -> cp.setElevationM(e.intValue()));
            checkpoints.add(cp);
        });

        // ── Detected peaks ─────────────────────────────────────────
        checkpoints.addAll(detectPeaks(segments, totalGain));

        // ── Sort by distance, number peaks ────────────────────────
        checkpoints.sort(Comparator.comparingDouble(SuggestedCheckpoint::getDistanceM));
        int peakCounter = 1;
        for (SuggestedCheckpoint cp : checkpoints) {
            if ("peak".equals(cp.getType())) {
                cp.setName("Top " + peakCounter++);
            }
        }

        analysis.setSuggestedCheckpoints(checkpoints);

        // ── Save GPX to PredictedSession as DRAFT ─────────────────
        PredictedSession session = new PredictedSession();
        session.setGpxData(bytes);
        session.setStatus("DRAFT");
        session.setDate(new Date());
        UserImpl user = userDAO.findById(userId).orElse(null);
        if (user != null) {
            session.setUser(user);
        }
        PredictedSession saved = predictedSessionDAO.save(session);

        // ── Build response ─────────────────────────────────────────
        RaceAnalysisResponse response = new RaceAnalysisResponse();
        response.setSessionId(saved.getId());
        response.setAnalysis(analysis);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ═══════════════════════════════════════════════════════════════
    // NEW: GENERATE PLAN — loads GPX from session, runs prediction,
    //      groups by checkpoints, saves JSON to sessionData
    // ═══════════════════════════════════════════════════════════════

    @PostMapping(path = "/generate")
    public ResponseEntity<String> generatePlan(
            @RequestBody GeneratePlanRequest request) throws Exception {

        // Load the draft session
        PredictedSession session = predictedSessionDAO
                .findById(request.getSessionId()).orElse(null);
        if (session == null) {
            return new ResponseEntity<>("Session not found", HttpStatus.NOT_FOUND);
        }

        UserImpl user = session.getUser();
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Run prediction on stored GPX
        List<PredictionModel> predictions = forecastManager.predictRun(
                new ByteArrayInputStream(session.getGpxData()), user);

        if (predictions == null || predictions.isEmpty()) {
            return new ResponseEntity<>("No predictions generated",
                    HttpStatus.NO_CONTENT);
        }

        // Build mobile session JSON
        MobileSession mobileSession = buildMobileSession(request, predictions);
        mobileSession.setSessionId(session.getId());
        // Save to session
        session.setName(request.getRaceName());
        session.setSessionData(
                new ObjectMapper().writeValueAsBytes(mobileSession));
        session.setStatus("READY");
        predictedSessionDAO.save(session);

        return new ResponseEntity<>(
                String.valueOf(session.getId()), HttpStatus.OK);
    }

    // ═══════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════════

    private MobileSession buildMobileSession(
            GeneratePlanRequest request,
            List<PredictionModel> predictions) {

        MobileSession mobile = new MobileSession();
        mobile.setRaceName(request.getRaceName());

        List<GeneratePlanRequest.Checkpoint> cps = request.getCheckpoints();
        cps.sort(Comparator.comparingDouble(
                GeneratePlanRequest.Checkpoint::getDistanceKm));

        List<MobileSegment> mobileSegments = new ArrayList<>();

        double prevDistM      = 0;
        double prevTimeModel2 = 0;  // cumulative time at end of previous section
        double prevTimeModel3 = 0;

        for (GeneratePlanRequest.Checkpoint cp : cps) {
            double cpDistM    = cp.getDistanceKm() * 1000;
            final double from = prevDistM;
            final double to   = cpDistM;

            // Find the LAST prediction within this section
            // Its value = cumulative time from race start to this point
            PredictionModel lastInSection = predictions.stream()
                    .filter(p -> p.getRanSegment().getTotalDistance() > from
                            && p.getRanSegment().getTotalDistance() <= to)
                    .max(Comparator.comparingDouble(
                            p -> p.getRanSegment().getTotalDistance()))
                    .orElse(null);

            if (lastInSection != null) {
                MobileSegment seg = new MobileSegment();
                seg.setName(cp.getName());
                seg.setDistanceFromStartM(cpDistM);

                // Cumulative time at end of this section
                double cumModel2 = lastInSection.getModel2();
                double cumModel3 = lastInSection.getModel3();

                // Section time = cumulative now - cumulative at previous section
                seg.setRunningTimeModel2((long) (cumModel2 - prevTimeModel2));
                seg.setRunningTimeModel3((long) (cumModel3 - prevTimeModel3));

                // GPS points for this section
                List<MobileGpsPoint> points = new ArrayList<>();
                predictions.stream()
                        .filter(p -> p.getRanSegment().getTotalDistance() > from
                                && p.getRanSegment().getTotalDistance() <= to)
                        .forEach(pm -> {
                            if (pm.getRanSegment().getSegment() != null
                                    && pm.getRanSegment().getSegment()
                                    .getWayPoints() != null) {
                                pm.getRanSegment().getSegment()
                                        .getWayPoints().forEach(wp -> {
                                            MobileGpsPoint pt = new MobileGpsPoint();
                                            pt.setLatitude(
                                                    wp.getLatitude().doubleValue());
                                            pt.setLongitude(
                                                    wp.getLongitude().doubleValue());
                                            wp.getElevation().ifPresent(e ->
                                                    pt.setAltitude(e.doubleValue()));
                                            points.add(pt);
                                        });
                            }
                        });
                seg.setGpsPoints(points);
                mobileSegments.add(seg);

                prevTimeModel2 = cumModel2;
                prevTimeModel3 = cumModel3;
            }

            prevDistM = cpDistM;
        }

        mobile.setSegments(mobileSegments);
        return mobile;
    }

    private double findClosestCumulativeDistance(
            List<Segment> segments, double lat, double lon) {
        double bestDist    = Double.MAX_VALUE;
        double bestCumDist = 0;
        double cumDist     = 0;
        for (Segment seg : segments) {
            cumDist += seg.getDistance();
            if (seg.getTo() != null) {
                double d = haversine(lat, lon,
                        seg.getTo().getLatitude().doubleValue(),
                        seg.getTo().getLongitude().doubleValue());
                if (d < bestDist) {
                    bestDist    = d;
                    bestCumDist = cumDist;
                }
            }
        }
        return bestCumDist;
    }

    private List<SuggestedCheckpoint> detectPeaks(
            List<Segment> segments, int totalGain) {

        List<SuggestedCheckpoint> peaks = new ArrayList<>();
        int window = 20;

        double[] elevations   = new double[segments.size()];
        double[] cumDistances = new double[segments.size()];
        double cumDist = 0;

        for (int i = 0; i < segments.size(); i++) {
            cumDist += segments.get(i).getDistance();
            cumDistances[i] = cumDist;
            elevations[i]   = segments.get(i).getElevationTo() != null
                    ? segments.get(i).getElevationTo() : 0;
        }

        // Pass 1 — find all local maxima
        List<Integer> candidates = new ArrayList<>();
        for (int i = window; i < segments.size() - window; i++) {
            double elev    = elevations[i];
            boolean isPeak = true;
            for (int j = i - window; j <= i + window; j++) {
                if (j != i && elevations[j] >= elev) {
                    isPeak = false;
                    break;
                }
            }
            if (isPeak) candidates.add(i);
        }

        // Pass 2 — filter by climb from previous valley
        int prevPeakIdx = 0;
        for (int c = 0; c < candidates.size(); c++) {
            int    peakIdx  = candidates.get(c);
            double peakElev = elevations[peakIdx];

            // Minimum between previous peak and this candidate
            double valleyMin = peakElev;
            for (int j = prevPeakIdx; j < peakIdx; j++) {
                valleyMin = Math.min(valleyMin, elevations[j]);
            }

            double climb = peakElev - valleyMin;

            System.out.println("Candidate peak: dist="
                    + String.format("%.0f", cumDistances[peakIdx])
                    + "m elev=" + (int) peakElev
                    + "m valley=" + (int) valleyMin
                    + "m climb=" + (int) climb + "m");

            if (climb >= 300) {
                SuggestedCheckpoint cp = new SuggestedCheckpoint();
                cp.setDistanceM(cumDistances[peakIdx]);
                cp.setElevationM((int) peakElev);
                cp.setType("peak");
                peaks.add(cp);
                System.out.println("  → Peak accepted");
            }

            prevPeakIdx = peakIdx;
        }
        return peaks;
    }

    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {
        final double R = 6_371_000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private SegmentForecast buildSegmentForecast(
            String name, PredictionModel prediction) {
        SegmentForecast seg = new SegmentForecast();
        seg.setName(name);
        seg.setDistance(String.format("%.2f",
                prediction.getRanSegment().getTotalDistance() / 1000));
        seg.setElevationGain(prediction.getRanSegment().getElevationGain());
        seg.setElevationLoss(prediction.getRanSegment().getElevationLoss());
        double m2 = prediction.getModel2();
        double m3 = prediction.getModel3();
        double m4 = prediction.getModel4();
        seg.setDuration2(m2);
        seg.setDuration3(m3);
        seg.setDuration4(m4);
        seg.setMin(TimeUtil.MakeReadable((long) Math.min(m2, m3)));
        seg.setMax(TimeUtil.MakeReadable((long) Math.max(m2, m3)));
        return seg;
    }

    private Forecast generateForecast(String name,
                                      List<PredictionModel> predictions,
                                      Double[] points, String[] names) {

        Forecast forecast = new Forecast();
        forecast.setName(name);
        double distance    = 0.0;
        int elevationGain  = 0;
        int elevationLoss  = 0;
        int pLen           = points.length;
        int nLen           = names.length;
        boolean useNames   = pLen == nLen;
        int counter        = 0;
        PredictionModel lastRecorded = null;
        PredictionModel last         = null;

        for (PredictionModel prediction : predictions) {
            distance      = prediction.getRanSegment().getTotalDistance();
            elevationGain = prediction.getRanSegment().getElevationGain();
            elevationLoss = prediction.getRanSegment().getElevationLoss();
            if (counter < pLen && distance >= points[counter] * 1000) {
                SegmentForecast segment =
                        addSegmentToForecast(prediction, forecast);
                String segName = useNames
                        ? names[counter]
                        : Double.toString(points[counter]);
                segment.setName(segName);
                lastRecorded = prediction;
                counter = nextCount(counter, points);
            }
            last = prediction;
        }
        if (last != lastRecorded) {
            SegmentForecast segment = addSegmentToForecast(last, forecast);
            segment.setName(String.format("%.2f",
                    last.getRanSegment().getTotalDistance() / 1000));
        }
        forecast.setDistance(distance);
        forecast.setElevationGain(elevationGain);
        forecast.setElevationLoss(elevationLoss);
        return forecast;
    }

    private int nextCount(int counter, Double[] points) {
        int previous = counter;
        counter++;
        while (counter < points.length
                && points[counter] < points[previous]) {
            counter++;
        }
        return counter;
    }

    private SegmentForecast addSegmentToForecast(
            PredictionModel prediction, Forecast forecast) {
        double distance   = prediction.getRanSegment().getTotalDistance();
        int elevationGain = prediction.getRanSegment().getElevationGain();
        int elevationLoss = prediction.getRanSegment().getElevationLoss();
        SegmentForecast segment = new SegmentForecast();
        forecast.add(segment);
        segment.setDistance(String.format("%.2f", distance / 1000));
        segment.setElevationGain(elevationGain);
        segment.setElevationLoss(elevationLoss);
        double min = Math.min(prediction.getModel2(), prediction.getModel3());
        double max = Math.max(prediction.getModel2(), prediction.getModel3());
        segment.setMin(TimeUtil.MakeReadable((long) min));
        segment.setMax(TimeUtil.MakeReadable((long) max));
        return segment;
    }
}