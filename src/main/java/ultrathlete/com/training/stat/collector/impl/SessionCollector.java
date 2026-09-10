package ultrathlete.com.training.stat.collector.impl;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.springframework.stereotype.Service;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.Session;
import ultrathlete.com.training.stat.collector.StatCollector;
import ultrathlete.com.training.visit.SegmentVisitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SessionCollector implements StatCollector {
    @Override
    public Session collect(String gpxFile, List<SegmentVisitor> visitors) {
        try {
            final GPX gpx = GPX.read(gpxFile);

            Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
            if (points.isPresent()) {
                List<WayPoint> list = points.get().getPoints();
                int i = 0;
                WayPoint prev = null;
                double totalDistance = 0.0;
                double segmentDistance = 0.0;
                WayPoint startSegment = null;
                WayPoint endSegment = null;
                List<RanSegment> segments = new ArrayList<>();
                WayPoint lastPoint = null;
                WayPoint lastSegment = null;
                Length distance = null;
                double totalUp = 0.0;
                double totalDown = 0.0;
                double totalFlat = 0.0;
                Long totalTimeUp = 0L;
                Long totalTimeDown = 0L;
                Long totalTimeFlat = 0L;

                for (WayPoint point : list) {

                    lastPoint = point;
                    if (startSegment == null) {
                        startSegment = point;
                    }
                    if (i++ > 0) {
                        distance = Geoid.WGS84.distance(prev, point);
                        totalDistance += distance.doubleValue();

                        if (segmentDistance + distance.doubleValue() <= 50.0) {
                            segmentDistance += distance.doubleValue();
                        } else {
                            endSegment = prev;
                            Segment segment = new Segment();
                            segment.setFrom(startSegment);
                            segment.setTo(endSegment);
                            segment.setDistance(segmentDistance);
                            if (startSegment.getElevation().isPresent()) {
                                segment.setElevationFrom(startSegment.getElevation().get().intValue());
                            }
                            if (endSegment.getElevation().isPresent()) {
                                segment.setElevationTo(endSegment.getElevation().get().intValue());
                            }
                            if (startSegment.getTime().isPresent() && endSegment.getTime().isPresent()) {
                                long from = startSegment.getTime().get().toInstant().toEpochMilli() * 1000;
                                long to = endSegment.getTime().get().toInstant().toEpochMilli() * 1000;
                                segment.setTime(to - from);
                            }
                            // Collect data
                            RanSegment ran = new RanSegment(segment);
                            for (SegmentVisitor visitor : visitors) {
                                segment.accept(visitor);
                                visitor.update(ran);
                            }
                            // Reinit
                            startSegment = prev;
                            endSegment = null;
                            segmentDistance = distance.doubleValue();
                            segments.add(ran);

                            lastSegment = point;
                        }
                    }
                    prev = point;
                }
                if (lastPoint != lastSegment) {
                    // Not added yet
                    Segment segment = new Segment();
                    segment.setFrom(startSegment);
                    segment.setTo(prev);
                    segment.setDistance(segmentDistance);
                    if (startSegment.getElevation().isPresent()) {
                        segment.setElevationFrom(startSegment.getElevation().get().intValue());
                    }
                    if (prev.getElevation().isPresent()) {
                        segment.setElevationTo(prev.getElevation().get().intValue());
                    }
                    if (startSegment.getTime().isPresent() && prev.getTime().isPresent()) {
                        long from = startSegment.getTime().get().toInstant().toEpochMilli();
                        long to = prev.getTime().get().toInstant().toEpochMilli();
                        segment.setTime(to - from);
                    }
                    // Collect data
                    RanSegment ran = new RanSegment(segment);
                    for (SegmentVisitor visitor : visitors) {
                        segment.accept(visitor);
                        visitor.update(ran);
                    }

                    segments.add(ran);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
