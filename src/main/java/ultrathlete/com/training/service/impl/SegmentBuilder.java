package ultrathlete.com.training.service.impl;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.SessionFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Take a route, and cut it in segments of a given length.
 */
public class SegmentBuilder {

    private List<Segment> segments = new ArrayList<>();
    private int lastElevation = 0;
    private int lastSegmentElevation = 0;
    private double segmentDistance = 0.0;
    private WayPoint prev = null;
    private WayPoint startSegment = null;
    private WayPoint endSegment = null;
    private WayPoint lastPoint = null;
    private WayPoint lastSegment = null;
    private Length distance = null;

    private void reInit(WayPoint point) {
        startSegment = prev;
        endSegment = null;
        segmentDistance = distance.doubleValue();
        lastSegment = point;
    }

    public List<Segment> getSegments(SessionFile file, int segmentLength) {
        if (file != null) {
            byte[] data = file.getData();
            if (data != null) {
                InputStream stream = new ByteArrayInputStream(data);
                return getSegments(stream, segmentLength);
            }
        }
        return new ArrayList<>();
    }

    public List<Segment> getSegments(InputStream file, int segmentLength) {

        try {
            final GPX gpx = GPX.read(file);

            Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
            if (points.isPresent()) {
                List<WayPoint> list = points.get().getPoints();
                int i = 0;

                List<WayPoint> wayPoints = new ArrayList<>();

                for (WayPoint point : list) {
                    lastElevation = point.getElevation().isPresent() ? point.getElevation().get().intValue() : lastElevation;
                    lastPoint = point;
                    startSegment = startSegment != null ? startSegment : point;
                    wayPoints.add(point);

                    if (i++ > 0) {
                        distance = Geoid.WGS84.distance(prev, point);   // Get the distance betwen two way points
                        if (segmentDistance + distance.doubleValue() <= segmentLength) {    // Add them until the segment
                            segmentDistance += distance.doubleValue();                      // length is reached
                        } else {    // New segment
                            endSegment = prev;
                            Segment segment = getSegmentData(startSegment, endSegment, segmentDistance,
                                    lastElevation, wayPoints);
                            segments.add(segment);
                            //System.out.println(segment.getDistance() + " - " + TimeUtil.MakeReadable(segment.getTime()));
                            reInit(point);
                            wayPoints = new ArrayList<>();
                        }
                    }
                    prev = point;
                }
                if (lastPoint != lastSegment) {     // Make sure the last point is added
                    Segment segment = getSegmentData(startSegment, prev, segmentDistance, lastElevation, wayPoints);
                    segments.add(segment);
                    //System.out.println("Last: " + segment.getDistance() + " - " + TimeUtil.MakeReadable(segment.getTime()));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return segments;
    }

    private Segment getSegmentData(WayPoint startPoint, WayPoint endPoint, double segmentDistance
            , int lastElevation, List<WayPoint> wayPoints) {

        Segment segment = new Segment();

        segment.setFrom(startPoint);
        segment.setTo(endPoint);
        segment.setWayPoints(wayPoints);
        segment.setDistance(segmentDistance);
        // Get the elevation
        if (startPoint.getElevation().isPresent()) {
            segment.setElevationFrom(startPoint.getElevation().get().intValue());
        } else {
            segment.setElevationFrom(lastSegmentElevation);
        }
        if (endPoint != null && endPoint.getElevation().isPresent()) {
            segment.setElevationTo(endPoint.getElevation().get().intValue());
            lastSegmentElevation = endPoint.getElevation().get().intValue();
        } else {
            segment.setElevationTo(lastElevation);
            lastSegmentElevation = lastElevation;
        }
        // Get the time
        if (startPoint.getTime().isPresent() && endPoint != null && endPoint.getTime().isPresent()) {
            long from = startPoint.getTime().get().toInstant().toEpochMilli() / 1000;
            long to = endPoint.getTime().get().toInstant().toEpochMilli() / 1000;
            segment.setTime(to - from);
        }
        return segment;
    }
}
