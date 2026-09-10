package ultrathlete.com.gps;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.junit.Test;
import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestGPS3 {

    public static void main(String[] args) {

        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
    }



    @Test
    public void readFromMultipleTimeLast150() {
        try {
            final String[] gpxs = {
                    "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                    "C:\\Users\\gille\\Downloads\\1.gpx",
                    "C:\\Users\\gille\\Downloads\\2.gpx",
                    "C:\\Users\\gille\\Downloads\\3.gpx",
                    "C:\\Users\\gille\\Downloads\\4.gpx",
                    "C:\\Users\\gille\\Downloads\\5.gpx",
                    "C:\\Users\\gille\\Downloads\\6.gpx",
                    "C:\\Users\\gille\\Downloads\\7.gpx",
                    "C:\\Users\\gille\\Downloads\\8.gpx",
                    "C:\\Users\\gille\\Downloads\\9.gpx",
                    "C:\\Users\\gille\\Downloads\\10.gpx",
                    "C:\\Users\\gille\\Downloads\\11.gpx",
                    "C:\\Users\\gille\\Downloads\\12.gpx",
                    "C:\\Users\\gille\\Downloads\\13.gpx",
                    "C:\\Users\\gille\\Downloads\\14.gpx",
                    "C:\\Users\\gille\\Downloads\\15.gpx",
                    "C:\\Users\\gille\\Downloads\\ultra.gpx",
                    "C:\\Users\\gille\\Downloads\\mont.gpx",
                    "C:\\Users\\gille\\Downloads\\bal.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                if (points.isPresent()) {
                    List<WayPoint> list = points.get().getPoints();
                    int i = 0;
                    WayPoint prev = null;
                    int elevationLast = 0;
                    int elevationBeforeLast = 0;
                    double totalDistance = 0.0;
                    double segmentDistance = 0.0;
                    WayPoint startSegment = null;
                    WayPoint endSegment = null;
                    List<RanSegment> segments = new ArrayList<>();
                    WayPoint lastPoint = null;
                    WayPoint lastSegment = null;
                    Length distance = null;
                    double totalUp = 0.0;
                    double totalVUp = 0.0;
                    double totalDown = 0.0;
                    double totalVDown = 0.0;
                    double totalFlat = 0.0;
                    Long totalTimeUp = 0L;
                    Long totalTimeVUp = 0L;
                    Long totalTimeDown = 0L;
                    Long totalTimeVDown = 0L;
                    Long totalTimeFlat = 0L;
                    Integer elevationGain = 0;
                    Integer elevationLoss = 0;
                    int last3 = 0;
                    int last2 = 0;
                    int last = 0;

                    RanSession session = new RanSession();
                    RanSegment previousRan = null;
                    RanSegment currentRan = null;
                    for (WayPoint point : list) {

                        lastPoint = point;
                        if (startSegment == null) {
                            startSegment = point;
                        }
                        if (i++ > 0) {
                            distance = Geoid.WGS84.distance(prev, point);
                            totalDistance += distance.doubleValue();

                            if (segmentDistance + distance.doubleValue() <= 400) {
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 4 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -4){
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                }
                                else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);

                                previousRan = currentRan;
                                currentRan = ran;
                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();
                                segments.add(ran);
                                totalSegments.add(ran);
                              //  printSegment(ran);

                                lastSegment = point;

                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);
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
                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                            long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                            segment.setTime(to - from);
                        }
                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                        if (rGrade >= 4 && rGrade < 12) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (rGrade >= 12) {
                            totalVUp += segmentDistance;
                            totalTimeVUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (rGrade < -12) {
                            totalVDown += segmentDistance;
                            totalTimeVDown += segment.getTime();
                            elevationLoss += segment.getElevation();
                        } else if (rGrade >= -12 && rGrade < -4){
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
                            elevationLoss += segment.getElevation();
                        }
                        else {
                            totalFlat += segmentDistance;
                            totalTimeFlat += segment.getTime();
                        }
                        RanSegment ran = new RanSegment(segment);
                        ran.setTotalUp(totalUp);
                        ran.setTotalDown(totalDown);
                        ran.setTotalFlat(totalFlat);
                        ran.setTotalTimeUp(totalTimeUp);
                        ran.setTotalTimeDown(totalTimeDown);
                        ran.setTotalTimeFlat(totalTimeFlat);
                        ran.setElevationGain(elevationGain);
                        ran.setElevationLoss(elevationLoss);
                        ran.setRanSession(session);
                        last3 = 0;
                        last2 = last;
                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                        ran.setLast150(last + last2 + last3);


                        segments.add(ran);
                        totalSegments.add(ran);
                        //printSegment(ran);
                    }

                    // All done. Record the session data
                    session.setRanSegments(segments);
                    session.setTotalDistanceDown(totalDown);
                    session.setTotalDistanceFlat(totalFlat);
                    session.setTotalDistanceUp(totalUp);
                    session.setTotalTimeDown(totalTimeDown);
                    session.setTotalTimeUp(totalTimeUp);
                    session.setTotalTimeFlat(totalTimeFlat);
                    session.setTotalElevationGain(elevationGain);
                    session.setTotalElevationLoss(elevationLoss);

                }
            }

            System.out.println("--- In scope ---------------------------------------");
            List<RanSegment> inscope = new ArrayList<>();

            for (RanSegment segment : totalSegments) {

                Double grade = segment.getGrade() * 100;
                if (grade >= 2.00 && grade < 4.00) {
                    inscope.add(segment);
                    printSegment(segment);
                }
            }
            int j = 0;
            double[][] xpoints = new double[inscope.size()][5];
            double[] ypoints = new double[inscope.size()];
            for (RanSegment segment : inscope) {
                xpoints[j][0] = 1;

/* 33 4-6
                xpoints[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                xpoints[j][2] = segment.getTotalFlat().doubleValue();
                xpoints[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                xpoints[j][4] = segment.getLast150().doubleValue();
*/
/* 95 % 2-4, 93% 4-6, 100% 6-8, 79% 8-10, 100% 10-12,
 Sur Ballaison
                xpoints[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                xpoints[j][2] = segment.getTotalFlat().doubleValue();
                xpoints[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                xpoints[j][4] = segment.getLast150().doubleValue();
*/
                xpoints[j][1] = segment.getTotalDistance().doubleValue();
//                xpoints[j][2] = segment.getTotalFlat().doubleValue();
//                xpoints[j][3] = segment.getTotalDown().doubleValue();
//                xpoints[j][4] = segment.getLast150().doubleValue();
                ypoints[j] = (segment.getSpeed());
                j++;
            }

            if (j > 0) {

                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

                /* Print results */
                System.out.println("\n>>Regression equation is:");
               // System.out.println("y = " + regression.beta(0) + " + "
               //         + regression.beta(1) + " x2 + " + regression.beta(2) + " x3 + "
               //         + regression.beta(3) + " x4 + " + regression.beta(4) + " x5\n");
                System.out.println(">>R^2 = " + (regression.R2()) * 100 + " %\n");
                System.out.println(">>SSE = " + regression.getSSE());
                System.out.println(">>SST = " + regression.getSST());
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readFromMultipleTypePlaineJoux() {

            final String[] gpxs = {
                    "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                    "C:\\Users\\gille\\Downloads\\pj1.gpx",
                    "C:\\Users\\gille\\Downloads\\pj2.gpx",
                    "C:\\Users\\gille\\Downloads\\pj3.gpx",
                    "C:\\Users\\gille\\Downloads\\pj4.gpx",
                    "C:\\Users\\gille\\Downloads\\pj5.gpx",
                    "C:\\Users\\gille\\Downloads\\pj6.gpx"
            };

            for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

                for (double segmentGrade = 0.0; segmentGrade <= 12; segmentGrade += 2.0) {

                    try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 4 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -4) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 4 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -4) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                        double[][] xpoints3 = new double[inscope.size()][3];
                        double[] ypoints3 = new double[inscope.size()];
                        double[][] xpoints4 = new double[inscope.size()][3];
                        double[] ypoints4 = new double[inscope.size()];
                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;


/*
65% avec 200m
82 % 2-4 pour 700m
26% 4-6
45% 6-8
22% 8-10
25% 10-12
70% 12-15  segment de 300 */
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

/*
0-2 63% sur 200
2-4 86% avec 800
4-6 58% avec 400
6-8 64% avec 500
8-10 51% avec 250
10-12 98% avec 400
12-15 300 99%
  */
                xpoints2[j][0] = 1;
                xpoints2[j][1] = segment.getTotalUp().doubleValue();
                xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                xpoints2[j][3] = segment.getTotalDown().doubleValue();
                xpoints2[j][4] = segment.getLast150().doubleValue();
                ypoints2[j] = (segment.getSpeed());

                        xpoints3[j][0] = 1;
                        xpoints3[j][1] = segment.getTotalDistance().doubleValue();
                        xpoints3[j][2] = segment.getLast150().doubleValue();
                        ypoints3[j] = (segment.getSpeed());

                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalTime().doubleValue();
                        xpoints2[j][2] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        j++;
                    }

                        if (j > 0) {

                            ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                            ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                            //ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                            //ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                            /* Print results */
                            System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100);

                            }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                }
            }
    }

    @Test
    public void readFromMultipleTypeMontagne() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\m1.gpx",
                "C:\\Users\\gille\\Downloads\\m2.gpx",
                "C:\\Users\\gille\\Downloads\\m3.gpx",
                "C:\\Users\\gille\\Downloads\\m4.gpx",
                "C:\\Users\\gille\\Downloads\\m5.gpx",
                "C:\\Users\\gille\\Downloads\\m6.gpx",
                "C:\\Users\\gille\\Downloads\\m7.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = 0.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 4 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -4) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 4 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -4) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    double[][] xpoints3 = new double[inscope.size()][3];
                    double[] ypoints3 = new double[inscope.size()];
                    double[][] xpoints4 = new double[inscope.size()][3];
                    double[] ypoints4 = new double[inscope.size()];
                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;


/*
65% avec 200m
82 % 2-4 pour 700m
26% 4-6
45% 6-8
22% 8-10
25% 10-12
70% 12-15  segment de 300 */
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

/*
0-2 63% sur 200
2-4 86% avec 800
4-6 58% avec 400
6-8 64% avec 500
8-10 51% avec 250
10-12 98% avec 400
12-15 300 99%
  */
                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        xpoints3[j][0] = 1;
                        xpoints3[j][1] = segment.getTotalDistance().doubleValue();
                        xpoints3[j][2] = segment.getLast150().doubleValue();
                        ypoints3[j] = (segment.getSpeed());

                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalTime().doubleValue();
                        xpoints2[j][2] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                        /* Print results */
                        System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100
                                + ";" + regression3.R2() * 100 + ";" + regression4.R2() * 100);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readFromMultipleTypePlaine() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                "C:\\Users\\gille\\Downloads\\plaine7.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = 0.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 4 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -4) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 4 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -4) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;


/*
65% avec 200m
82 % 2-4 pour 700m
26% 4-6
45% 6-8
22% 8-10
25% 10-12
70% 12-15  segment de 300 */
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

/*
0-2 63% sur 200
2-4 86% avec 800
4-6 58% avec 400
6-8 64% avec 500
8-10 51% avec 250
10-12 98% avec 400
12-15 300 99%
  */
                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);

                        /* Print results */
                        System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void printSegment(RanSegment segment) {
//        System.out.println("D: " + segment.getDistance() + ";" +
//                "T: " + segment.getTime() + ";" +
//                "G: " + segment.getGrade() * 100 + ";" +
//                "E: " + segment.getElevation()+ ";" +
//                "EG: " + segment.getElevationGain() + ";" +
//                "EL: " + segment.getElevationLoss() + ";" +
//                "TU: " + segment.getTotalUp() + ";" +
//                "TVU: " + segment.getTotalVUp() + ";" +
//                "TD: " + segment.getTotalDown() + ";" +
//                "TVD: " + segment.getTotalVDown() + ";" +
//                "TF: " + segment.getTotalFlat()+ ";" +
//                "TiU: " + segment.getTotalTimeUp() + ";" +
//                "TiVU: " + segment.getTotalTimeVUp() + ";" +
//                "TiD: " + segment.getTotalTimeDown() + ";" +
//                "TiVD: " + segment.getTotalTimeVDown());

        System.out.println(segment.getDistance() + ";" +
                 segment.getTime() + ";" +
                segment.getGrade() * 100 + ";" +
                segment.getElevation()+ ";" +
                segment.getElevationGain() + ";" +
                segment.getElevationLoss() + ";" +
                segment.getTotalUp() + ";" +
                segment.getTotalVUp() + ";" +
                segment.getTotalDown() + ";" +
                segment.getTotalVDown() + ";" +
                segment.getTotalFlat()+ ";" +
                segment.getTotalTimeUp() + ";" +
                segment.getTotalTimeVUp() + ";" +
                segment.getTotalTimeDown() + ";" +
                segment.getTotalTimeVDown());
    }

    @Test
    public void readAll() {

        final String[] gpxs = {
//                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine7.gpx",
//                "C:\\Users\\gille\\Downloads\\m1.gpx",
//                "C:\\Users\\gille\\Downloads\\m2.gpx",
//                "C:\\Users\\gille\\Downloads\\m3.gpx",
//                "C:\\Users\\gille\\Downloads\\m4.gpx",
//                "C:\\Users\\gille\\Downloads\\m5.gpx",
//                "C:\\Users\\gille\\Downloads\\m6.gpx",
//                "C:\\Users\\gille\\Downloads\\m7.gpx",
                "C:\\Users\\gille\\Downloads\\pj1.gpx",
                "C:\\Users\\gille\\Downloads\\pj2.gpx",
                "C:\\Users\\gille\\Downloads\\pj3.gpx",
                "C:\\Users\\gille\\Downloads\\pj4.gpx",
                "C:\\Users\\gille\\Downloads\\pj5.gpx",
                "C:\\Users\\gille\\Downloads\\pj6.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

              try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 4 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -4) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 4 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -4) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    int j = 0;
                    double[][] xpoints1 = new double[totalSegments.size()][6];
                    double[] ypoints1 = new double[totalSegments.size()];
                    double[][] xpoints2 = new double[totalSegments.size()][6];
                    double[] ypoints2 = new double[totalSegments.size()];
                    for (RanSegment segment : totalSegments) {
                        xpoints1[j][0] = 1;


/*
65% avec 200m
82 % 2-4 pour 700m
26% 4-6
45% 6-8
22% 8-10
25% 10-12
70% 12-15  segment de 300 */
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        xpoints1[j][5] = segment.getGrade();
                        ypoints1[j] = (segment.getSpeed());

/*
0-2 63% sur 200
2-4 86% avec 800
4-6 58% avec 400
6-8 64% avec 500
8-10 51% avec 250
10-12 98% avec 400
12-15 300 99%
  */
                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        xpoints1[j][5] = segment.getGrade();
                        ypoints2[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);

                        /* Print results */
                        System.out.println(segmentLength + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }



    @Test
    public void readCircuits() {
        try {
            final String[] gpxs = {
                    "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                    "C:\\Users\\gille\\Downloads\\1.gpx",
                    "C:\\Users\\gille\\Downloads\\2.gpx",
                    "C:\\Users\\gille\\Downloads\\3.gpx",
                    "C:\\Users\\gille\\Downloads\\4.gpx",
                    "C:\\Users\\gille\\Downloads\\5.gpx",
                    "C:\\Users\\gille\\Downloads\\6.gpx",
                    "C:\\Users\\gille\\Downloads\\7.gpx",
                    "C:\\Users\\gille\\Downloads\\8.gpx",
                    "C:\\Users\\gille\\Downloads\\9.gpx",
                    "C:\\Users\\gille\\Downloads\\10.gpx",
                    "C:\\Users\\gille\\Downloads\\ultra.gpx",
                    "C:\\Users\\gille\\Downloads\\mont.gpx",
                    "C:\\Users\\gille\\Downloads\\bal.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
                System.out.println("------------------------------------------------------");
                System.out.println(gpxs[g]);
                System.out.println("Path length: " + length);

                Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                if (points.isPresent()) {
                    List<WayPoint> list = points.get().getPoints();
                    int i = 0;
                    WayPoint prev = null;
                    int elevationLast = 0;
                    int elevationBeforeLast = 0;
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
                    Integer elevationGain = 0;
                    Integer elevationLoss = 0;
                    int last3 = 0;
                    int last2 = 0;
                    int last = 0;

                    RanSession session = new RanSession();
                    RanSegment previousRan = null;
                    RanSegment currentRan = null;
                    for (WayPoint point : list) {

                        lastPoint = point;
                        if (startSegment == null) {
                            startSegment = point;
                        }
                        if (i++ > 0) {
                            distance = Geoid.WGS84.distance(prev, point);
                            totalDistance += distance.doubleValue();

                            if (segmentDistance + distance.doubleValue() <= 400.0) {
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                if (Grade.GetGradeReference(segment.getGrade()) * 100 >= 2) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (Grade.GetGradeReference(segment.getGrade()) * 100 < -2) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);

                                previousRan = currentRan;
                                currentRan = ran;
                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();
                                segments.add(ran);
                                totalSegments.add(ran);

                                lastSegment = point;

                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);
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
                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                            long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                            segment.setTime(to - from);
                        }
                        if (Grade.GetGradeReference(segment.getGrade()) * 100 >= 2) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (Grade.GetGradeReference(segment.getGrade()) * 100 < -2) {
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
                            elevationLoss += segment.getElevation();
                        } else {
                            totalFlat += segmentDistance;
                            totalTimeFlat += totalTimeUp += segment.getTime();
                        }
                        RanSegment ran = new RanSegment(segment);
                        ran.setTotalUp(totalUp);
                        ran.setTotalDown(totalDown);
                        ran.setTotalFlat(totalFlat);
                        ran.setTotalTimeUp(totalTimeUp);
                        ran.setTotalTimeDown(totalTimeDown);
                        ran.setTotalTimeFlat(totalTimeFlat);
                        ran.setElevationGain(elevationGain);
                        ran.setElevationLoss(elevationLoss);
                        ran.setRanSession(session);
                        last3 = 0;
                        last2 = last;
                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                        ran.setLast150(last + last2 + last3);


                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                    // All done. Record the session data
                    session.setRanSegments(segments);
                    session.setTotalDistanceDown(totalDown);
                    session.setTotalDistanceFlat(totalFlat);
                    session.setTotalDistanceUp(totalUp);
                    session.setTotalTimeDown(totalTimeDown);
                    session.setTotalTimeUp(totalTimeUp);
                    session.setTotalTimeFlat(totalTimeFlat);
                    session.setTotalElevationGain(elevationGain);
                    session.setTotalElevationLoss(elevationLoss);
                    System.out.println("U: " + session.getTotalDistanceUp());
                    System.out.println("F: " + session.getTotalDistanceFlat());
                    System.out.println("D: " + session.getTotalDistanceDown());
                    System.out.println("TU: " + session.getTotalTimeUp());
                    System.out.println("TF: " + session.getTotalTimeFlat());
                    System.out.println("TD: " + session.getTotalTimeDown());
                    System.out.println("G: " + session.getElevationGain());
                    System.out.println("L: " + session.getElevationLoss());
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readFromMultipleTypePlaineJoux2() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\pj1.gpx",
                "C:\\Users\\gille\\Downloads\\pj2.gpx",
                "C:\\Users\\gille\\Downloads\\pj3.gpx",
                "C:\\Users\\gille\\Downloads\\pj4.gpx",
                "C:\\Users\\gille\\Downloads\\pj5.gpx",
                "C:\\Users\\gille\\Downloads\\pj6.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = 0.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 2 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -2) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 2 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -2) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    double[][] xpoints3 = new double[inscope.size()][3];
                    double[] ypoints3 = new double[inscope.size()];
                    double[][] xpoints4 = new double[inscope.size()][3];
                    double[] ypoints4 = new double[inscope.size()];
                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;


/*
65% avec 200m
82 % 2-4 pour 700m
26% 4-6
45% 6-8
22% 8-10
25% 10-12
70% 12-15  segment de 300 */
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

/*
0-2 63% sur 200
2-4 86% avec 800
4-6 58% avec 400
6-8 64% avec 500
8-10 51% avec 250
10-12 98% avec 400
12-15 300 99%
  */
                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                        //ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                        //ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                        /* Print results */
                        System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100);

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readFromMultipleTypeMontagne2() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\m1.gpx",
                "C:\\Users\\gille\\Downloads\\m2.gpx",
                "C:\\Users\\gille\\Downloads\\m3.gpx",
                "C:\\Users\\gille\\Downloads\\m4.gpx",
                "C:\\Users\\gille\\Downloads\\m5.gpx",
                "C:\\Users\\gille\\Downloads\\m6.gpx",
                "C:\\Users\\gille\\Downloads\\m7.gpx"
        };

        for (Integer segmentLength = 50; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = -10.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 2 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -2) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 3 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -3) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    double[][] xpoints3 = new double[inscope.size()][5];
                    double[] ypoints3 = new double[inscope.size()];
                    double[][] xpoints4 = new double[inscope.size()][5];
                    double[] ypoints4 = new double[inscope.size()];

                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        xpoints3[j][0] = 1;
                        xpoints3[j][1] = segment.getTotalTimeUp().doubleValue() * segment.getElevationGain();
                        xpoints3[j][2] = segment.getTotalTimeFlat().doubleValue();
                        xpoints3[j][3] = segment.getTotalTimeDown().doubleValue() * segment.getElevationLoss();
                        xpoints3[j][4] = segment.getLast150().doubleValue();
                        ypoints3[j] = (segment.getSpeed());

                        xpoints4[j][0] = 1;
                        xpoints4[j][1] = segment.getTotalTimeUp().doubleValue();
                        xpoints4[j][2] = segment.getTotalTimeFlat().doubleValue();
                        xpoints4[j][3] = segment.getTotalTimeDown().doubleValue();
                        xpoints4[j][4] = segment.getLast150().doubleValue();
                        ypoints4[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                        /* Print results */
                        System.out.println(segmentLength + "\t" + segmentGrade + "\t" + String.format("%.2f", regression1.R2() * 100) + "\t"
                                + String.format("%.2f", regression2.R2() * 100)
                                + "\t" + String.format("%.2f", regression3.R2() * 100)
                                + "\t" + String.format("%.2f", regression4.R2() * 100));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readFromMultipleTypePlaine2() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                "C:\\Users\\gille\\Downloads\\plaine7.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = -10.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 2 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -2) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 2 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -2) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    double[][] xpoints3 = new double[inscope.size()][4];
                    double[] ypoints3 = new double[inscope.size()];
                    double[][] xpoints4 = new double[inscope.size()][5];
                    double[] ypoints4 = new double[inscope.size()];

                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        xpoints3[j][0] = 1;
                        xpoints3[j][1] = segment.getTotalDistance().doubleValue();
                        xpoints3[j][2] = segment.getElevationGain().doubleValue();
                        xpoints3[j][3] = segment.getLast150().doubleValue();
                        ypoints3[j] = (segment.getSpeed());

                        xpoints4[j][0] = 1;
                        xpoints4[j][1] = segment.getTotalTimeUp().doubleValue();
                        xpoints4[j][2] = segment.getTotalTimeFlat().doubleValue();
                        xpoints4[j][3] = segment.getTotalTimeDown().doubleValue();
                        xpoints4[j][4] = segment.getLast150().doubleValue();
                        ypoints4[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                        /* Print results */
                        System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100
                                + ";" + regression3.R2() * 100 + ";" + regression4.R2() * 100);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Test
    public void readFromMultipleTypeAll2() {

        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\pj1.gpx",
                "C:\\Users\\gille\\Downloads\\pj2.gpx",
                "C:\\Users\\gille\\Downloads\\pj3.gpx",
                "C:\\Users\\gille\\Downloads\\pj4.gpx",
                "C:\\Users\\gille\\Downloads\\pj5.gpx",
                "C:\\Users\\gille\\Downloads\\pj6.gpx",
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\m1.gpx",
                "C:\\Users\\gille\\Downloads\\m2.gpx",
                "C:\\Users\\gille\\Downloads\\m3.gpx",
                "C:\\Users\\gille\\Downloads\\m4.gpx",
                "C:\\Users\\gille\\Downloads\\m5.gpx",
                "C:\\Users\\gille\\Downloads\\m6.gpx",
                "C:\\Users\\gille\\Downloads\\m7.gpx",
                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                "C:\\Users\\gille\\Downloads\\plaine7.gpx"
        };

        for (Integer segmentLength = 100; segmentLength <= 1000; segmentLength += 100) {

            for (double segmentGrade = -10.0; segmentGrade <= 12; segmentGrade += 2.0) {

                try {

                    List<RanSegment> totalSegments = new ArrayList<>();

                    for (int g = 0; g < gpxs.length; g++) {

                        final GPX gpx = GPX.read(gpxs[g]);
                        final Length length = gpx.tracks()
                                .flatMap(Track::segments)
                                .findFirst()
                                .map(TrackSegment::points).orElse(Stream.empty())
                                .collect(Geoid.WGS84.toPathLength());
//                System.out.println("------------------------------------------------------");
//                System.out.println(gpxs[g]);
//                System.out.println("Path length: " + length);

                        Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                        if (points.isPresent()) {
                            List<WayPoint> list = points.get().getPoints();
                            int i = 0;
                            WayPoint prev = null;
                            int elevationLast = 0;
                            int elevationBeforeLast = 0;
                            double totalDistance = 0.0;
                            double segmentDistance = 0.0;
                            WayPoint startSegment = null;
                            WayPoint endSegment = null;
                            List<RanSegment> segments = new ArrayList<>();
                            WayPoint lastPoint = null;
                            WayPoint lastSegment = null;
                            Length distance = null;
                            double totalUp = 0.0;
                            double totalVUp = 0.0;
                            double totalDown = 0.0;
                            double totalVDown = 0.0;
                            double totalFlat = 0.0;
                            Long totalTimeUp = 0L;
                            Long totalTimeVUp = 0L;
                            Long totalTimeDown = 0L;
                            Long totalTimeVDown = 0L;
                            Long totalTimeFlat = 0L;
                            Integer elevationGain = 0;
                            Integer elevationLoss = 0;
                            int last3 = 0;
                            int last2 = 0;
                            int last = 0;

                            RanSession session = new RanSession();
                            RanSegment previousRan = null;
                            RanSegment currentRan = null;
                            for (WayPoint point : list) {

                                lastPoint = point;
                                if (startSegment == null) {
                                    startSegment = point;
                                }
                                if (i++ > 0) {
                                    distance = Geoid.WGS84.distance(prev, point);
                                    totalDistance += distance.doubleValue();

                                    if (segmentDistance + distance.doubleValue() <= segmentLength) {
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
                                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                            segment.setTime(to - from);
                                        }
                                        // Sums
                                        Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                        if (rGrade >= 2 && rGrade < 12) {
                                            totalUp += segmentDistance;
                                            totalTimeUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade >= 12) {
                                            totalVUp += segmentDistance;
                                            totalTimeVUp += segment.getTime();
                                            elevationGain += segment.getElevation();
                                        } else if (rGrade < -12) {
                                            totalVDown += segmentDistance;
                                            totalTimeVDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else if (rGrade >= -12 && rGrade < -2) {
                                            totalDown += segmentDistance;
                                            totalTimeDown += segment.getTime();
                                            elevationLoss += segment.getElevation();
                                        } else {
                                            totalFlat += segmentDistance;
                                            totalTimeFlat += segment.getTime();
                                        }
                                        RanSegment ran = new RanSegment(segment);
                                        ran.setTotalUp(totalUp);
                                        ran.setTotalDown(totalDown);
                                        ran.setTotalFlat(totalFlat);
                                        ran.setTotalTimeUp(totalTimeUp);
                                        ran.setTotalTimeDown(totalTimeDown);
                                        ran.setTotalTimeFlat(totalTimeFlat);
                                        ran.setElevationGain(elevationGain);
                                        ran.setElevationLoss(elevationLoss);
                                        ran.setRanSession(session);

                                        previousRan = currentRan;
                                        currentRan = ran;
                                        // Reinit
                                        startSegment = prev;
                                        endSegment = null;
                                        segmentDistance = distance.doubleValue();
                                        segments.add(ran);
                                        totalSegments.add(ran);
                                        //  printSegment(ran);

                                        lastSegment = point;

                                        last3 = 0;
                                        last2 = last;
                                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                        ran.setLast150(last + last2 + last3);
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                                if (rGrade >= 2 && rGrade < 12) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade >= 12) {
                                    totalVUp += segmentDistance;
                                    totalTimeVUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (rGrade < -12) {
                                    totalVDown += segmentDistance;
                                    totalTimeVDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else if (rGrade >= -12 && rGrade < -2) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
                                    elevationLoss += segment.getElevation();
                                } else {
                                    totalFlat += segmentDistance;
                                    totalTimeFlat += segment.getTime();
                                }
                                RanSegment ran = new RanSegment(segment);
                                ran.setTotalUp(totalUp);
                                ran.setTotalDown(totalDown);
                                ran.setTotalFlat(totalFlat);
                                ran.setTotalTimeUp(totalTimeUp);
                                ran.setTotalTimeDown(totalTimeDown);
                                ran.setTotalTimeFlat(totalTimeFlat);
                                ran.setElevationGain(elevationGain);
                                ran.setElevationLoss(elevationLoss);
                                ran.setRanSession(session);
                                last3 = 0;
                                last2 = last;
                                last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                                ran.setLast150(last + last2 + last3);


                                segments.add(ran);
                                totalSegments.add(ran);
                                //printSegment(ran);
                            }

                            // All done. Record the session data
                            session.setRanSegments(segments);
                            session.setTotalDistanceDown(totalDown);
                            session.setTotalDistanceFlat(totalFlat);
                            session.setTotalDistanceUp(totalUp);
                            session.setTotalTimeDown(totalTimeDown);
                            session.setTotalTimeUp(totalTimeUp);
                            session.setTotalTimeFlat(totalTimeFlat);
                            session.setTotalElevationGain(elevationGain);
                            session.setTotalElevationLoss(elevationLoss);

                        }
                    }

                    List<RanSegment> inscope = new ArrayList<>();

                    for (RanSegment segment : totalSegments) {

                        Double grade = segment.getGrade() * 100;
                        if (grade >= segmentGrade && grade < segmentGrade + 2.0) {
                            inscope.add(segment);
                            //printSegment(segment);
                        }
                    }
                    int j = 0;
                    double[][] xpoints1 = new double[inscope.size()][5];
                    double[] ypoints1 = new double[inscope.size()];
                    double[][] xpoints2 = new double[inscope.size()][5];
                    double[] ypoints2 = new double[inscope.size()];
                    double[][] xpoints3 = new double[inscope.size()][4];
                    double[] ypoints3 = new double[inscope.size()];
                    double[][] xpoints4 = new double[inscope.size()][5];
                    double[] ypoints4 = new double[inscope.size()];

                    for (RanSegment segment : inscope) {
                        xpoints1[j][0] = 1;
                        xpoints1[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain();
                        xpoints1[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints1[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                        xpoints1[j][4] = segment.getLast150().doubleValue();
                        ypoints1[j] = (segment.getSpeed());

                        xpoints2[j][0] = 1;
                        xpoints2[j][1] = segment.getTotalUp().doubleValue();
                        xpoints2[j][2] = segment.getTotalFlat().doubleValue();
                        xpoints2[j][3] = segment.getTotalDown().doubleValue();
                        xpoints2[j][4] = segment.getLast150().doubleValue();
                        ypoints2[j] = (segment.getSpeed());

                        xpoints3[j][0] = 1;
                        xpoints3[j][1] = segment.getTotalDistance().doubleValue();
                        xpoints3[j][2] = segment.getElevationGain().doubleValue();
                        xpoints3[j][3] = segment.getLast150().doubleValue();
                        ypoints3[j] = (segment.getSpeed());

                        xpoints4[j][0] = 1;
                        xpoints4[j][1] = segment.getTotalTimeUp().doubleValue();
                        xpoints4[j][2] = segment.getTotalTimeFlat().doubleValue();
                        xpoints4[j][3] = segment.getTotalTimeDown().doubleValue();
                        xpoints4[j][4] = segment.getLast150().doubleValue();
                        ypoints4[j] = (segment.getSpeed());

                        j++;
                    }

                    if (j > 0) {

                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                        ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);

                        /* Print results */
                        System.out.println(segmentLength + ";" + segmentGrade + ";" + regression1.R2() * 100 + ";" + regression2.R2() * 100
                                + ";" + regression3.R2() * 100 + ";" + regression4.R2() * 100);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
