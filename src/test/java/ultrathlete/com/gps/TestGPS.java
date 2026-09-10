package ultrathlete.com.gps;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.junit.Test;
import ultrathlete.com.data.stats.Utils.MultiplePoint;
import ultrathlete.com.data.stats.linearRegression.MultipleLinearRegression;
import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;

import java.io.IOException;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.stream.Stream;

import static ultrathlete.com.data.stats.regression.MultipleLinearRegression.confidenceInterval;

public class TestGPS {

    public static void main(String[] args) {

        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
    }

    @Test
    public void calculateDistance() {
        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
    }

    @Test
    public void calculatePathLength() {
        final GPX gpx = GPX.builder()
                .addTrack(track -> track
                        .addSegment(segment -> segment
                                .addPoint(p -> p.lat(48.2081743).lon(16.3738189).ele(160))
                                .addPoint(p -> p.lat(48.2080000).lon(16.3730000).ele(161))
                                .addPoint(p -> p.lat(48.2081111).lon(16.3799999).ele(2000))))
                .build();
        final Length length = gpx.tracks()
                .flatMap(Track::segments)
                .findFirst()
                .map(TrackSegment::points).orElse(Stream.empty())
                .collect(Geoid.WGS84.toPathLength());
        System.out.println(length);
    }

    @Test
    public void readFromPlaineJoux() {
        try {
            final GPX gpx = GPX.read("C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx");
            final Length length = gpx.tracks()
                    .flatMap(Track::segments)
                    .findFirst()
                    .map(TrackSegment::points).orElse(Stream.empty())
                    .collect(Geoid.WGS84.toPathLength());
            System.out.println(length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFromSkiDeFond() {
        try {
            final GPX gpx = GPX.read("C:\\Users\\gille\\Downloads\\plainejouxcap.gpx");
            //final GPX gpx = GPX.read("C:\\Users\\gille\\Downloads\\activity_8345001159.gpx");   // Plaine joux
            //final GPX gpx = GPX.read("C:\\Users\\gille\\Downloads\\activity_8322337373.gpx"); // SDF
            final Length length = gpx.tracks()
                    .flatMap(Track::segments)
                    .findFirst()
                    .map(TrackSegment::points).orElse(Stream.empty())
                    .collect(Geoid.WGS84.toPathLength());
            System.out.println("Path length: " + length);

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
                            // Sums
                            if (segment.getElevation() > 0) {
                                totalUp += segmentDistance;
                                totalTimeUp += segment.getTime();
                            } else if (segment.getElevation() < 0) {
                                totalDown += segmentDistance;
                                totalTimeDown += segment.getTime();
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
                            // Reinit
                            startSegment = prev;
                            endSegment = null;
                            segmentDistance = distance.doubleValue();
                            segments.add(ran);

                            lastSegment = point;
                        }

                        System.out.println(distance.doubleValue() + " " + totalDistance + " " + totalDistance % 50.0 +
                                " Segment distance: " + segmentDistance);
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
                    if (segment.getElevation() > 0) {
                        totalUp += segmentDistance;
                        totalTimeUp += segment.getTime();
                    } else if (segment.getElevation() < 0) {
                        totalDown += segmentDistance;
                        totalTimeDown += segment.getTime();
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

                    segments.add(ran);
                }
/*
                System.out.println(i + " " + list.size() + " Total: " + totalDistance);
                System.out.println("--------------------------------------------------");
                double totalSegmentDistance = 0.0;
                for (RanSegment segment : segments) {
                    System.out.println("D: " + segment.getDistance() + "\tE: " + segment.getElevation() + "\tT: " + segment.getTime()
                        + "\tG: " + segment.getGrade() * 100 + "\tU: " + segment.getTotalUp()
                        + "\tD: " + segment.getTotalDown() + "\tF: " + totalFlat + "\tS: " + segment.getSpeed()
                            + "\tTU: " + segment.getTotalTimeUp()
                            + "\tTD: " + segment.getTotalTimeDown() + "\tTF: " + totalTimeFlat);
                    totalSegmentDistance += segment.getDistance();
                }
                System.out.println("Total segments: " + totalSegmentDistance);

                System.out.println("Grades--------------------------------------------------");
                for (RanSegment segment : segments) {
                    System.out.println("G: " + segment.getGrade() * 100);
                }
*/
                List<MultiplePoint> rpoints = new ArrayList<>();
                double[][] xpoints = new double[segments.size()][4];
                double[] ypoints = new double[segments.size()];
                int j = 0;
                for (RanSegment segment : segments) {
                    Double grade = segment.getGrade();

                    if (grade >= -0.04 && grade < -0.02) {
                        List<Double> params = new ArrayList<>();
                        params.add(segment.getTotalUp());
                        params.add(segment.getTotalFlat());
                        params.add(segment.getTotalDown());
                        rpoints.add(new MultiplePoint(params, segment.getSpeed()));

                        xpoints[j][0] = 1;
                        xpoints[j][1] = segment.getTotalUp();
                        xpoints[j][2] = segment.getTotalFlat();
                        xpoints[j][3] = segment.getTotalDown();
                        ypoints[j] = segment.getSpeed();
                        j++;
                    }
                }
                //creates the multiple linear regression object and computes the coefficients
                MultipleLinearRegression mlr = new MultipleLinearRegression(rpoints);
                //get the coefficients
                double[][] coefficients = mlr.getCoefficients();
                for (int c = 0; c < coefficients.length; c++) {
                    System.out.println(coefficients[c][0]);
                }

                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

                /* Print results */
                System.out.println("\n>>Regression equation is:");
                System.out.println("y = "+ regression.beta(0) + " + "
                        + regression.beta(1) + " x2 + " + regression.beta(2) + " x3 + "
                        + regression.beta(3) + " x5\n");
                System.out.println(">>R^2 = " + (regression.R2())*100 + " %\n");
                System.out.println(">>SSE = " + regression.getSSE());
                System.out.println(">>SST = " + regression.getSST());

                System.out.println(coefficients);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readFromMultiple() {
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
                    "C:\\Users\\gille\\Downloads\\10.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
                System.out.println("Path length: " + length);

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
                                // Sums
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                } else if (segment.getElevation() < 0) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
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
                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();
                                segments.add(ran);
                                totalSegments.add(ran);

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
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                        } else if (segment.getElevation() < 0) {
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
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

                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                }
            }

            List<MultiplePoint> rpoints = new ArrayList<>();
            double[][] xpoints = new double[totalSegments.size()][4];
            double[] ypoints = new double[totalSegments.size()];
            int j = 0;
            for (RanSegment segment : totalSegments) {
                Double grade = segment.getGrade();

                if (grade >= -0.12 && grade < -0.10) {
                    List<Double> params = new ArrayList<>();
                    params.add(segment.getTotalUp());
                    params.add(segment.getTotalFlat());
                    params.add(segment.getTotalDown());
                    rpoints.add(new MultiplePoint(params, segment.getSpeed()));

                    xpoints[j][0] = 1;
                    xpoints[j][1] = segment.getTotalUp();
                    xpoints[j][2] = segment.getTotalFlat();
                    xpoints[j][3] = segment.getTotalDown();
                    ypoints[j] = segment.getSpeed();
                    j++;
                }
            }
            //creates the multiple linear regression object and computes the coefficients
            //MultipleLinearRegression mlr = new MultipleLinearRegression(rpoints);
            //get the coefficients
            //double[][] coefficients = mlr.getCoefficients();
            //for (int c = 0; c < coefficients.length; c++) {
            //    System.out.println(coefficients[c][0]);
            //}

            ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

            /* Print results */
            System.out.println("\n>>Regression equation is:");
            System.out.println("y = "+ regression.beta(0) + " + "
                    + regression.beta(1) + " x2 + " + regression.beta(2) + " x3 + "
                    + regression.beta(3) + " x4\n");
            System.out.println(">>R^2 = " + (regression.R2())*100 + " %\n");
            System.out.println(">>SSE = " + regression.getSSE());
            System.out.println(">>SST = " + regression.getSST());

            } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readFromMultipleTime() {
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
                    "C:\\Users\\gille\\Downloads\\10.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                } else if (segment.getElevation() < 0) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
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
                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();
                                segments.add(ran);
                                totalSegments.add(ran);

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
                            long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                            long to = prev.getTime().get().toInstant().toEpochMilli() / 1000;
                            segment.setTime(to - from);
                        }
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                        } else if (segment.getElevation() < 0) {
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
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

                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                }
            }

            List<MultiplePoint> rpoints = new ArrayList<>();
            double[][] xpoints = new double[totalSegments.size()][4];
            double[] ypoints = new double[totalSegments.size()];
            int j = 0;
            for (RanSegment segment : totalSegments) {
                Double grade = segment.getGrade();

                if (grade >= 0.00 && grade < 0.20) {
                    List<Double> params = new ArrayList<>();
                    params.add(segment.getTotalTimeUp().doubleValue());
                    params.add(segment.getTotalTimeFlat().doubleValue());
                    params.add(segment.getTotalTimeDown().doubleValue());
                    rpoints.add(new MultiplePoint(params, segment.getSpeed()));

                    xpoints[j][0] = 1;
                    xpoints[j][1] = segment.getTotalTimeUp().doubleValue();
                    xpoints[j][2] = segment.getTotalTimeFlat().doubleValue();
                    xpoints[j][3] = segment.getTotalTimeDown().doubleValue();
                    ypoints[j] = segment.getSpeed();
                    j++;
                }
            }
            //creates the multiple linear regression object and computes the coefficients
            //MultipleLinearRegression mlr = new MultipleLinearRegression(rpoints);
            //get the coefficients
            //double[][] coefficients = mlr.getCoefficients();
            //for (int c = 0; c < coefficients.length; c++) {
            //    System.out.println(coefficients[c][0]);
            //}

            ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

            /* Print results */
            System.out.println("\n>>Regression equation is:");
            System.out.println("y = "+ regression.beta(0) + " + "
                    + regression.beta(1) + " x2 + " + regression.beta(2) + " x3 + "
                    + regression.beta(3) + " x4\n");
            System.out.println(">>R^2 = " + (regression.R2())*100 + " %\n");
            System.out.println(">>SSE = " + regression.getSSE());
            System.out.println(">>SST = " + regression.getSST());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readFromMultipleFlat() {
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
                    "C:\\Users\\gille\\Downloads\\10.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
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
                    int elevationGain = 0;
                    int elevationLoss = 0;

                    int last3 = 0;
                    int last2 = 0;
                    int last = 0;

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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (segment.getElevation() < 0) {
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
                                previousRan = currentRan;
                                currentRan = ran;
                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();
                                segments.add(ran);
                                totalSegments.add(ran);

                                lastSegment = point;

                                last3 = last2;
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
                            long from = startSegment.getTime().get().toInstant().toEpochMilli();
                            long to = prev.getTime().get().toInstant().toEpochMilli();
                            segment.setTime(to - from);
                        }
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                        } else if (segment.getElevation() < 0) {
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
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

                        last3 = last2;
                        last2 = last;
                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                        ran.setLast150(last + last2 + last3);


                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                }
            }

            List<MultiplePoint> rpoints = new ArrayList<>();
            double[][] xpoints = new double[totalSegments.size()][4];
            double[] ypoints = new double[totalSegments.size()];
            int j = 0;
            for (RanSegment segment : totalSegments) {
                Double grade = segment.getGrade();

                System.out.println(segment.getSpeed() + "\t" + segment.getTotalTimeUp() + "\t" + segment.getTotalTimeFlat()
                        + "\t" + segment.getTotalTimeDown() + "\t" + segment.getLast150());

                if (grade >= 0.02 && grade < 0.04) {
                    xpoints[j][0] = 1;
                    xpoints[j][1] = segment.getTotalDistance().doubleValue();
                    xpoints[j][2] = segment.getElevationGain() != null ? segment.getElevationGain().doubleValue() : 0.0;
                    xpoints[j][3] = segment.getLast150() != null ? segment.getLast150().doubleValue() : 0.0;
                    ypoints[j] = segment.getSpeed();
                    j++;
                }
            }
            //creates the multiple linear regression object and computes the coefficients
            //MultipleLinearRegression mlr = new MultipleLinearRegression(rpoints);
            //get the coefficients
            //double[][] coefficients = mlr.getCoefficients();
            //for (int c = 0; c < coefficients.length; c++) {
            //    System.out.println(coefficients[c][0]);
            //}

            ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

            /* Print results */
            System.out.println("\n>>Regression equation is:");
            System.out.println("y = "+ regression.beta(0) + " + "
                    + regression.beta(1) + " x2\n");
            System.out.println(">>R^2 = " + (regression.R2())*100 + " %\n");
            System.out.println(">>SSE = " + regression.getSSE());
            System.out.println(">>SST = " + regression.getSST());

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void readFromMultipleTimeLast150() {
        try {
            final String[] gpxs = {
//                    "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
//                    "C:\\Users\\gille\\Downloads\\1.gpx",
//                    "C:\\Users\\gille\\Downloads\\2.gpx",
//                    "C:\\Users\\gille\\Downloads\\3.gpx",
//                    "C:\\Users\\gille\\Downloads\\4.gpx",
//                    "C:\\Users\\gille\\Downloads\\5.gpx",
//                    "C:\\Users\\gille\\Downloads\\6.gpx",
//                    "C:\\Users\\gille\\Downloads\\7.gpx",
//                    "C:\\Users\\gille\\Downloads\\8.gpx",
//                    "C:\\Users\\gille\\Downloads\\9.gpx",
//                    "C:\\Users\\gille\\Downloads\\10.gpx",
//                    "C:\\Users\\gille\\Downloads\\ultra.gpx",
//                    "C:\\Users\\gille\\Downloads\\mont.gpx",
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
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (segment.getElevation() < 0) {
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
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (segment.getElevation() < 0) {
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
                }
            }

            List<RanSegment> inscope = new ArrayList<>();

            for (RanSegment segment : totalSegments) {
                Double grade = segment.getGrade();
                if (grade >= 0.00 && grade < 2.00) {
                    inscope.add(segment);

//                    System.out.println(segment.getElevationGain() + ", " +
//                        segment.getElevationLoss() + ", " + segment.getRanSession().getElevationGain()
//                            + ", " + segment.getRanSession().getElevationLoss() + ", " + segment.getGrade());
                }
            }
            int j = 0;
            double[][] xpoints = new double[inscope.size()][8];
            double[] ypoints = new double[inscope.size()];
            for (RanSegment segment : inscope) {
                xpoints[j][0] = 1;
//                xpoints[j][1] = segment.getTotalTimeUp().doubleValue();
//                xpoints[j][2] = segment.getTotalTimeFlat().doubleValue();
//                xpoints[j][3] = segment.getTotalTimeDown().doubleValue();
//                xpoints[j][4] = segment.getLast150().doubleValue();

//                xpoints[j][1] = segment.getElevationGain();   // 41
//                xpoints[j][2] = segment.getTotalFlat().doubleValue();
//                xpoints[j][3] = segment.getElevationLoss();
//                xpoints[j][4] = segment.getLast150().doubleValue();

//                xpoints[j][1] = segment.getTotalTimeUp().doubleValue() * segment.getElevationGain(); //46
//                xpoints[j][2] = segment.getTotalTimeFlat().doubleValue() * segment.getTotalFlat();
//                xpoints[j][3] = segment.getTotalTimeDown().doubleValue() * segment.getElevationLoss();
//                xpoints[j][4] = segment.getLast150().doubleValue();

                xpoints[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain(); //65
                xpoints[j][2] = segment.getTotalFlat().doubleValue();
                xpoints[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss();
                xpoints[j][4] = segment.getLast150().doubleValue();
                xpoints[j][5] = (segment.getRanSession().getTotalDistanceUp() - segment.getTotalUp().doubleValue()) * (segment.getRanSession().getElevationGain() - segment.getElevationGain());
                xpoints[j][6] = segment.getRanSession().getTotalDistanceFlat() - segment.getTotalFlat().doubleValue();
                xpoints[j][7] = (segment.getRanSession().getTotalDistanceDown() - segment.getTotalDown().doubleValue()) * (segment.getRanSession().getElevationLoss() - segment.getElevationLoss());
//                // Rajouter le % qui reste de plat montée descente ou le denivellé restant en chiffres bruts

//                xpoints[j][1] = segment.getTotalDistance().doubleValue();
//                xpoints[j][2] = segment.getLast150().doubleValue();


//                xpoints[j][1] = segment.getTotalUp().doubleValue() * segment.getElevationGain() * segment.getTotalTimeUp(); //62
//                xpoints[j][2] = segment.getTotalFlat() * segment.getTotalTimeFlat();
//                xpoints[j][3] = segment.getTotalDown().doubleValue() * segment.getElevationLoss() * segment.getTotalTimeDown();
//                xpoints[j][4] = segment.getLast150().doubleValue();

                ypoints[j] = Math.log(segment.getSpeed());
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
    public void calculateAll() {
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
                    "C:\\Users\\gille\\Downloads\\10.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                } else if (segment.getElevation() < 0) {
                                    totalDown += segmentDistance;
                                    totalTimeDown += segment.getTime();
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
                                last2 = 0;
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
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                        } else if (segment.getElevation() < 0) {
                            totalDown += segmentDistance;
                            totalTimeDown += segment.getTime();
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

                        last3 = last2;
                        last2 = last;
                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                        ran.setLast150(last + last2 + last3);


                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                }
            }


            Map<Grade, List<RanSegment>> allSegments = new HashMap<>();

            for (RanSegment segment : totalSegments) {
                Grade grade = Grade.GetGradeLevel(segment.getGrade());
                List<RanSegment> list = allSegments.get(grade);
                if (list == null) {
                    list = new ArrayList<>();
                    allSegments.put(grade, list);
                }
                list.add(segment);
            }
            // Tous les segments sont groupés par pente
            // On fait une régression sur chaque pente
            Set<Grade> grades = allSegments.keySet();
            for (Grade grade : grades) {
                System.out.println("---- " + grade + "-------------------------------------------------");
                List<RanSegment> segments = allSegments.get(grade);
                System.out.println("---- " + segments.size());
                double[][] xpoints = new double[segments.size()][5];
                double[] ypoints = new double[segments.size()];
                int j = 0;
                for (RanSegment segment : segments) {

                        xpoints[j][0] = 1;
                        xpoints[j][1] = segment.getTotalTimeUp().doubleValue();
                        xpoints[j][2] = segment.getTotalTimeFlat().doubleValue();
                        xpoints[j][3] = segment.getTotalTimeDown().doubleValue();
                        xpoints[j][4] = segment.getLast150().doubleValue();
                        ypoints[j] = segment.getSpeed();
                        j++;
                }

                if (j > 0) {

                    ultrathlete.com.data.stats.regression.MultipleLinearRegression regression = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints, ypoints);

                    /* Print results */
                    System.out.println("\n>>Regression equation is:");
                    System.out.println("y = " + regression.beta(0) + " + "
                            + regression.beta(1) + " x2 + " + regression.beta(2) + " x3 + "
                            + regression.beta(3) + " x4 + " + regression.beta(4) + " x5\n");
                    System.out.println(">>R^2 = " + (regression.R2()) * 100 + " %\n");
                    System.out.println(">>SSE = " + regression.getSSE());
                    System.out.println(">>SST = " + regression.getSST());
                }
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void withGrade() {
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
                    "C:\\Users\\gille\\Downloads\\10.gpx"
            };

            List<RanSegment> totalSegments = new ArrayList<>();

            for (int g = 0; g < gpxs.length; g++) {

                final GPX gpx = GPX.read(gpxs[g]);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());
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
                                    long from = startSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    long to = endSegment.getTime().get().toInstant().toEpochMilli() / 1000;
                                    segment.setTime(to - from);
                                }
                                // Sums
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (segment.getElevation() < 0) {
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
                                last2 = 0;
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
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (segment.getElevation() < 0) {
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
                        ran.setElevationLoss(elevationLoss);
                        ran.setElevationGain(elevationGain);

                        last3 = last2;
                        last2 = last;
                        last = previousRan != null ? previousRan.getElevationTo() - previousRan.getElevationFrom() : 0;

                        ran.setLast150(last + last2 + last3);


                        segments.add(ran);
                        totalSegments.add(ran);
                    }

                }
            }

            int j = 0;
            double[][] xpoints = new double[totalSegments.size()][5];
            double[] ypoints = new double[totalSegments.size()];
            for (RanSegment segment : totalSegments) {
                xpoints[j][0] = 1;
                xpoints[j][1] = segment.getGrade().doubleValue();
                xpoints[j][2] = segment.getElevationGain().doubleValue();
                xpoints[j][3] = segment.getTotalTime().doubleValue();
                xpoints[j][4] = segment.getLast150().doubleValue();
                //xpoints[j][5] = segment.getTotalTimeFlat().doubleValue();
                ypoints[j] = segment.getSpeed();
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
    public void readFromIntensite() {
        try {
            final GPX gpx = GPX.read("C:\\Users\\gille\\Downloads\\activity_8302560959.gpx");
            final Length length = gpx.tracks()
                    .flatMap(Track::segments)
                    .findFirst()
                    .map(TrackSegment::points).orElse(Stream.empty())
                    .collect(Geoid.WGS84.toPathLength());
            System.out.println(length);
        } catch (IOException e) {
            e.printStackTrace();
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
                                if (segment.getElevation() > 0) {
                                    totalUp += segmentDistance;
                                    totalTimeUp += segment.getTime();
                                    elevationGain += segment.getElevation();
                                } else if (segment.getElevation() < 0) {
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
                        if (segment.getElevation() > 0) {
                            totalUp += segmentDistance;
                            totalTimeUp += segment.getTime();
                            elevationGain += segment.getElevation();
                        } else if (segment.getElevation() < 0) {
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
}
