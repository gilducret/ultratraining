package ultrathlete.com.gps;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.junit.Test;
import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;

import java.util.*;
import java.util.stream.Stream;

public class TestGPS2 {

    public static void main(String[] args) {

        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
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
                "C:\\Users\\gille\\Downloads\\cretes.gpx"
//                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine7.gpx",
//        "C:\\Users\\gille\\Downloads\\ultra.gpx",
//                "C:\\Users\\gille\\Downloads\\orcier.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
//                "C:\\Users\\gille\\Downloads\\long1.gpx",
//                "C:\\Users\\gille\\Downloads\\allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\cfer.gpx"
        };


/*        final String[] gpxs = {
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice1-145.gpx"
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice2.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice3.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice4.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice5.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice6.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice7.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice8.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice9.gpx"
        };
*/
        final Boolean[] race = {
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false,
                 false,
                false
        };

        for (int l = 100; l <= 1000; l += 100) {      // 100, par 100
            for (int nbS = 10; nbS <= 50; nbS+=5) { // 10 par 10
                System.out.print(l + "; " + nbS + "; ");

                List<RanSegment> allRanSegments = new ArrayList<>();
                for (int g = 0; g < gpxs.length; g++) {
                    // Segments
                    List<Segment> segments = getSegments(gpxs[g], l);
                    Boolean isRace = race[g];
//            for (Segment segment : segments) {
//                printSegment(segment);
//            }
//                    System.out.println("---------------------------------------");
                    // Ran segments
                    List<RanSegment> ranSegments = getRanSegments(segments, nbS, isRace);
//            for (RanSegment ranSegment : ranSegments) {
//                printRanSegment(ranSegment);
//            }
                    allRanSegments.addAll(ranSegments);
                }
                calculateRegression(allRanSegments);
            }
        }
    }

    private void calculateRegression(List<RanSegment> ranSegments) {

        //System.out.println("--- Regression ---");

        int j = 0;
        double[][] xpoints1 = new double[ranSegments.size()][9];
        double[] ypoints1 = new double[ranSegments.size()];
        double[][] xpoints2 = new double[ranSegments.size()][8];
        double[] ypoints2 = new double[ranSegments.size()];
        double[][] xpoints3 = new double[ranSegments.size()][13];
        double[] ypoints3 = new double[ranSegments.size()];
        double[][] xpoints4 = new double[ranSegments.size()][6];
        double[] ypoints4 = new double[ranSegments.size()];
        double[][] xpoints5 = new double[ranSegments.size()][10];
        double[] ypoints5 = new double[ranSegments.size()];
        double[][] xpoints6 = new double[ranSegments.size()][10];
        double[] ypoints6 = new double[ranSegments.size()];
        double[][] xpoints7 = new double[ranSegments.size()][8];
        double[] ypoints7 = new double[ranSegments.size()];
        double[][] xpoints8 = new double[ranSegments.size()][13];
        double[] ypoints8 = new double[ranSegments.size()];
        double[][] xpoints9 = new double[ranSegments.size()][10];
        double[] ypoints9 = new double[ranSegments.size()];

        for (RanSegment segment : ranSegments) {
            xpoints1[j][0] = 1;
            xpoints2[j][0] = 1;
            xpoints3[j][0] = 1;
            xpoints4[j][0] = 1;
            xpoints5[j][0] = 1;
            xpoints6[j][0] = 1;
            xpoints7[j][0] = 1;
            xpoints8[j][0] = 1;
            xpoints9[j][0] = 1;

            /* Ne marche pas
            xpoints1[j][1] = segment.getTotalEasy();
            xpoints1[j][2] = segment.getTotalMedium();
            xpoints1[j][3] = segment.getTotalHard();
            //xpoints1[j][4] = segment.getTotalVeryHard();
            xpoints1[j][4] = segment.getTotalMediumD();
            xpoints1[j][5] = segment.getTotalHardD();
            //xpoints1[j][7] = segment.getTotalVeryHardD();
            xpoints1[j][6] = segment.getTotalEasySegment();
            xpoints1[j][7] = segment.getTotalMediumSegment();
            xpoints1[j][8] = segment.getTotalHardSegment();
            //xpoints1[j][11] = segment.getTotalVeryHardSegment();
            xpoints1[j][9] = segment.getTotalMediumDSegment();
            xpoints1[j][10] = segment.getTotalHardDSegment();
            //xpoints1[j][14] = segment.getTotalVeryHardDSegment(); */

            /* 35%
            xpoints1[j][1] = segment.getTotalDistance();
            xpoints1[j][2] = segment.getTotalMediumSegment() + segment.getTotalHardSegment() + segment.getTotalVeryHardSegment();
            xpoints1[j][3] = segment.getTotalMediumDSegment() + segment.getTotalHardDSegment() + segment.getTotalVeryHardDSegment();
*/
            // A rajouter: distance restante + les 300 prochains metres + l'elevation du prochain

            double totalUp = segment.getTotalMedium() + segment.getTotalHard() + segment.getTotalVeryHard();
            double totalDown = segment.getTotalMediumD() + segment.getTotalHardD() + segment.getTotalVeryHardD();
            double totalTimeUp = segment.getTotalTimeMedium() + segment.getTotalTimeHard() + segment.getTotalTimeVeryHard();
            double totalTimeDown = segment.getTotalTimeMediumD() + segment.getTotalTimeHardD() + segment.getTotalTimeVeryHardD();
            int nextElevationGain = segment.getNext() != null ? segment.getNext().getElevationGainSegment() : 0;
            double nextUp = segment.getNext() != null ? segment.getNext().getTotalMedium() + segment.getNext().getTotalHard() + segment.getNext().getTotalVeryHard() : 0;
            int nextElevationLoss = segment.getNext() != null ? segment.getNext().getElevationLossSegment() : 0;
            double nextDown = segment.getNext() != null ? segment.getNext().getTotalMediumD() + segment.getNext().getTotalHardD() + segment.getNext().getTotalVeryHardD() : 0;
            double nextEasy = segment.getNext() != null ? segment.getNext().getTotalEasy() : 0.0;
            double nextMedium = segment.getNext() != null ? segment.getNext().getTotalMedium() : 0.0;
            double nextHard = segment.getNext() != null ? segment.getNext().getTotalHard() : 0.0;
            double nextVeryHard = segment.getNext() != null ? segment.getNext().getTotalVeryHard() : 0.0;

            RanSession session = (RanSession) segment.getRanSession();

            double sessionDistance = session.getTotalEasy() +
            session.getTotalMedium()+
            session.getTotalHard()+
            session.getTotalVeryHard()+
            session.getTotalEasyD()+
            session.getTotalMediumD()+
            session.getTotalHardD()+
            session.getTotalVeryHardD();

            double sessionElevation = session.getElevationGain();

            // 46%
//            xpoints1[j][1] = totalUp * segment.getElevationGain();
//            xpoints1[j][2] = segment.getTotalEasy();
//            xpoints1[j][3] = totalDown * segment.getElevationLoss();
//            xpoints1[j][4] = (segment.getTotalMediumSegment() + segment.getTotalHardSegment() + segment.getTotalVeryHardSegment()) * segment.getElevationGainSegment();
//            xpoints1[j][5] = (segment.getTotalMediumDSegment() + segment.getTotalHardDSegment() + segment.getTotalVeryHardDSegment()) * segment.getElevationLossSegment();
//            xpoints1[j][6] = segment.getTotalEasySegment();
//            xpoints1[j][7] = nextElevationGain * nextUp;
//            xpoints1[j][8] = nextElevationLoss * nextDown;

            int elevHard = segment.getNext() != null ? segment.getNext().getTotalHardSegment() > 0 ? 1 : 0 : 0;
            int elevVHard = segment.getNext() != null ? segment.getNext().getTotalVeryHardSegment() > 0 ? 2 : 0 : 0;

            // 46 %
//            xpoints1[j][1] = totalUp * segment.getElevationGain();
//            xpoints1[j][2] = segment.getTotalEasy();
//            xpoints1[j][3] = totalDown * segment.getElevationLoss();
//            xpoints1[j][4] = (segment.getTotalMediumSegment() + segment.getTotalHardSegment() + segment.getTotalVeryHardSegment()) * segment.getElevationGainSegment();
//            xpoints1[j][5] = (segment.getTotalMediumDSegment() + segment.getTotalHardDSegment() + segment.getTotalVeryHardDSegment()) * segment.getElevationLossSegment();
//            xpoints1[j][6] = segment.getTotalEasySegment();
//            xpoints1[j][7] = elevHard + elevVHard;

            // 55
//            xpoints1[j][1] = totalUp;
//            xpoints1[j][2] = segment.getTotalEasy();
//            xpoints1[j][3] = totalDown;
//            xpoints1[j][4] = segment.getElevationGainSegment();
//            xpoints1[j][5] = segment.getElevationLossSegment();
//            xpoints1[j][6] = segment.getTotalEasySegment();
//            xpoints1[j][7] = elevHard + elevVHard;

            // 55
//            xpoints1[j][1] = totalTimeUp;
//            xpoints1[j][2] = segment.getTotalTimeEasy();
//            xpoints1[j][3] = totalTimeDown;
//            xpoints1[j][4] = segment.getElevationGainSegment();
//            xpoints1[j][5] = segment.getElevationLossSegment();
//            xpoints1[j][6] = segment.getTotalEasySegment();
//            xpoints1[j][7] = elevHard + elevVHard;

            // 58
//            xpoints1[j][1] =(segment.getTotalTime());
//            xpoints1[j][2] =(segment.getElevationGain());
//            xpoints1[j][3] = (segment.getElevationLoss());
//            xpoints1[j][4] = (segment.getElevationGainSegment());
//            xpoints1[j][5] = (segment.getElevationLossSegment());
//            ypoints1[j] =  Math.log(segment.getSpeed());

            // 60
            xpoints1[j][1] =(segment.getTotalTime());
            xpoints1[j][2] =(segment.getElevationGain());
            xpoints1[j][3] = (segment.getElevationLoss());
            xpoints1[j][4] = (segment.getElevationGainSegment());
            xpoints1[j][5] = (segment.getElevationLossSegment());
            xpoints1[j][6] = (segment.getNext300());
            xpoints1[j][7] = nextUp;
            xpoints1[j][8] = nextDown;
            ypoints1[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            xpoints2[j][1] = totalUp;
            xpoints2[j][2] = segment.getTotalEasy();
            xpoints2[j][3] = totalDown;
            xpoints2[j][4] = segment.getElevationGainSegment();
            xpoints2[j][5] = segment.getElevationLossSegment();
            xpoints2[j][6] = segment.getTotalEasySegment();
            xpoints2[j][7] = elevHard + elevVHard;
            ypoints2[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            xpoints3[j][1] = totalUp;
            xpoints3[j][2] = segment.getTotalEasy();
            xpoints3[j][3] = totalDown;
            xpoints3[j][4] = segment.getElevationGainSegment();
            xpoints3[j][5] = segment.getElevationLossSegment();
            xpoints3[j][6] = segment.getTotalEasySegment();
            xpoints3[j][7] = nextUp;
            xpoints3[j][8] = nextDown;
            xpoints3[j][9] = nextEasy;
            xpoints3[j][10] = segment.getNext300();
            xpoints3[j][11] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints3[j][12] = sessionDistance;
            ypoints3[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            xpoints4[j][1] = segment.getTotalDistance();
            xpoints4[j][2] = segment.getElevationGain();
            xpoints4[j][3] = segment.getElevationLoss();
            xpoints4[j][4] = segment.getElevationGainSegment();
            xpoints4[j][5] = segment.getElevationLossSegment();
            ypoints4[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            xpoints5[j][1] = totalUp;
            xpoints5[j][2] = segment.getTotalEasy();
            xpoints5[j][3] = totalDown;
            xpoints5[j][4] = segment.getElevationGainSegment();
            xpoints5[j][5] = segment.getElevationLossSegment();
            xpoints5[j][6] = segment.getTotalEasySegment();
            xpoints5[j][7] = segment.getNext300();
            xpoints5[j][8] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints5[j][9] = sessionDistance;
            ypoints5[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            // Pas mal sur du plat sur longs segments
            xpoints6[j][1] = segment.getTotalMedium();
            xpoints6[j][2] = segment.getTotalEasy();
            xpoints6[j][3] = segment.getTotalMediumD();
            xpoints6[j][4] = segment.getElevationGainSegment();
            xpoints6[j][5] = segment.getElevationLossSegment();
            xpoints6[j][6] = segment.getTotalEasySegment();
            xpoints6[j][7] = segment.getNext300();
            xpoints6[j][8] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints6[j][9] = sessionDistance;
            ypoints6[j] =  segment.getSegmentTime(); //Math.log(segment.getTime());

            xpoints7[j][1] = segment.getTotalDistance();
            xpoints7[j][2] = segment.getElevationGainSegment();
            xpoints7[j][3] = segment.getElevationLossSegment();
            xpoints7[j][4] = segment.getTotalEasySegment();
            xpoints7[j][5] = segment.getNext300();
            xpoints7[j][6] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints7[j][7] = sessionDistance;
            ypoints7[j] =  segment.getSegmentTime(); //Math.log(segment.getTime());

            xpoints8[j][1] = totalTimeUp;
            xpoints8[j][2] = segment.getTotalTimeEasy();
            xpoints8[j][3] = totalTimeDown;
            xpoints8[j][4] = segment.getElevationGainSegment();
            xpoints8[j][5] = segment.getElevationLossSegment();
            xpoints8[j][6] = segment.getTotalEasySegment();
            xpoints8[j][7] = nextUp;
            xpoints8[j][8] = nextDown;
            xpoints8[j][9] = nextEasy;
            xpoints8[j][10] = segment.getNext300();
            xpoints8[j][11] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints8[j][12] = sessionDistance;
            ypoints8[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            xpoints9[j][1] = totalTimeUp;
            xpoints9[j][2] = segment.getTotalTimeEasy();
            xpoints9[j][3] = totalTimeDown;
            xpoints9[j][4] = segment.getElevationGainSegment();
            xpoints9[j][5] = segment.getElevationLossSegment();
            xpoints9[j][6] = segment.getTotalEasySegment();
            xpoints9[j][7] = segment.getNext300();
            xpoints9[j][8] = segment.getPrevious() != null ? segment.getPrevious().getLast300() : 0.0;
            xpoints9[j][9] = sessionDistance;
            ypoints9[j] =  segment.getSegmentTime(); //Math.log(segment.getSpeed());

            //55
//            xpoints1[j][1] =(segment.getTotalDistance());
//            xpoints1[j][2] =(segment.getElevationGain());
//            xpoints1[j][3] = (segment.getElevationLoss());
//            xpoints1[j][4] = (segment.getElevationGainSegment());
//            xpoints1[j][5] = (segment.getElevationLossSegment());
//            xpoints1[j][6] = nextEasy;
//            xpoints1[j][7] = nextUp;
//            xpoints1[j][8] = nextDown;
//            xpoints1[j][9] = (segment.getTotalDistance() / sessionDistance) * 100;
//            ypoints1[j] =  (segment.getSpeed());

            j++;
        }

        if (j > 0) {

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                if (regression1.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression1.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                if (regression2.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression2.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                if (regression3.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression3.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);
                if (regression4.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression4.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression5 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints5, ypoints5);
                if (regression5.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression5.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression6 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints6, ypoints6);
                if (regression6.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression6.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression7 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints7, ypoints7);
                if (regression7.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression7.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression8 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints8, ypoints8);
                if (regression8.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression8.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression9 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints9, ypoints9);
                if (regression9.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.println(String.format("%3.2f", regression9.R2() * 100) + "; ");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<Segment> getSegments(String fileName, int segmentLength) {

        List<Segment> segments = new ArrayList<>();
        try {


                final GPX gpx = GPX.read(fileName);
                final Length length = gpx.tracks()
                        .flatMap(Track::segments)
                        .findFirst()
                        .map(TrackSegment::points).orElse(Stream.empty())
                        .collect(Geoid.WGS84.toPathLength());

                Optional<TrackSegment> points = gpx.tracks().flatMap(Track::segments).findFirst();
                if (points.isPresent()) {
                    List<WayPoint> list = points.get().getPoints();
                    int i = 0;
                    WayPoint prev = null;

                    double segmentDistance = 0.0;
                    WayPoint startSegment = null;
                    WayPoint endSegment = null;

                    WayPoint lastPoint = null;
                    WayPoint lastSegment = null;
                    Length distance = null;

                    for (WayPoint point : list) {

                        lastPoint = point;
                        if (startSegment == null) {
                            startSegment = point;
                        }
                        if (i++ > 0) {
                            distance = Geoid.WGS84.distance(prev, point);

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

                                segments.add(segment);

                                // Reinit
                                startSegment = prev;
                                endSegment = null;
                                segmentDistance = distance.doubleValue();


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
                        segments.add(segment);
                        //printSegment(segment);
                    }
                }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return segments;
    }

    private List<RanSegment> getRanSegments(List<Segment> segments, int nbSegments, boolean isRace) {
        List<RanSegment> ranSegments = new ArrayList<>();

        // Calculated variables
        double totalEasySegment = 0.0;
        double totalMediumSegment = 0.0;
        double totalHardSegment = 0.0;
        double totalVeryHardSegment = 0.0;

        double totalEasyDSegment = 0.0;
        double totalMediumDSegment = 0.0;
        double totalHardDSegment = 0.0;
        double totalVeryHardDSegment = 0.0;

        long totalTimeEasySegment = 0L;
        long totalTimeMediumSegment = 0L;
        long totalTimeHardSegment = 0L;
        long totalTimeVeryHardSegment = 0L;

        long totalTimeEasyDSegment = 0L;
        long totalTimeMediumDSegment = 0L;
        long totalTimeHardDSegment = 0L;
        long totalTimeVeryHardDSegment = 0L;

        long totalTimeEasy = 0L;
        long totalTimeMedium = 0L;
        long totalTimeHard = 0L;
        long totalTimeVeryHard = 0L;

        long totalTimeEasyD = 0L;
        long totalTimeMediumD = 0L;
        long totalTimeHardD = 0L;
        long totalTimeVeryHardD = 0L;

        int elevationGain = 0;
        int elevationLoss = 0;
        int elevationGainSegment = 0;
        int elevationLossSegment = 0;

        double totalEasy = 0.0;
        double totalMedium = 0.0;
        double totalHard = 0.0;
        double totalVeryHard = 0.0;

        double totalEasyD = 0.0;
        double totalMediumD = 0.0;
        double totalHardD = 0.0;
        double totalVeryHardD = 0.0;

        int next300 = 0;
        int last300 = 0;
        int altitude = 0;

        double speedBefore = 0.0;

        int i = 1;
        RanSegment previous = null;
        RanSession session = new RanSession();
        session.setRace(isRace);
        for (Segment segment : segments) {
            // Add all calculated variables
            // Sums
            if (i == 1) {
                altitude = segment.getElevationFrom();
            }
            if ((i > 0 && (i % nbSegments == 0))) {
                RanSegment ranSegment = new RanSegment();
                ranSegments.add(ranSegment);

                if (previous != null) {
                    previous.setNext(ranSegment);
                }
                ranSegment.setPrevious(previous);
                // next
                previous = ranSegment;
                // Set the calculated variables
                ranSegment.setAltitude(segment.getElevationFrom());
                ranSegment.setTotalEasySegment(totalEasySegment);
                ranSegment.setTotalMediumSegment(totalMediumSegment);
                ranSegment.setTotalHardSegment(totalHardSegment);
                ranSegment.setTotalVeryHardSegment(totalVeryHardSegment);

                ranSegment.setTotalEasyDSegment(totalEasyDSegment);
                ranSegment.setTotalMediumDSegment(totalMediumDSegment);
                ranSegment.setTotalHardDSegment(totalHardDSegment);
                ranSegment.setTotalVeryHardDSegment(totalVeryHardDSegment);

                ranSegment.setTotalTimeEasySegment(totalTimeEasySegment);
                ranSegment.setTotalTimeMediumSegment(totalTimeMediumSegment);
                ranSegment.setTotalTimeHardSegment(totalTimeHardSegment);
                ranSegment.setTotalTimeVeryHardSegment(totalTimeVeryHardSegment);

                ranSegment.setTotalTimeEasyDSegment(totalTimeEasyDSegment);
                ranSegment.setTotalTimeMediumDSegment(totalTimeMediumDSegment);
                ranSegment.setTotalTimeHardDSegment(totalTimeHardDSegment);
                ranSegment.setTotalTimeVeryHardDSegment(totalTimeVeryHardDSegment);

                ranSegment.setTotalTimeEasy(totalTimeEasy);
                ranSegment.setTotalTimeMedium(totalTimeMedium);
                ranSegment.setTotalTimeHard(totalTimeHard);
                ranSegment.setTotalTimeVeryHard(totalTimeVeryHard);

                ranSegment.setTotalTimeEasyD(totalTimeEasyD);
                ranSegment.setTotalTimeMediumD(totalTimeMediumD);
                ranSegment.setTotalTimeHardD(totalTimeHardD);
                ranSegment.setTotalTimeVeryHardD(totalTimeVeryHardD);

                ranSegment.setElevationGain(elevationGain);
                ranSegment.setElevationLoss(elevationLoss);
                ranSegment.setElevationGainSegment(elevationGainSegment);
                ranSegment.setElevationLossSegment(elevationLossSegment);

                ranSegment.setTotalEasy(totalEasy);
                ranSegment.setTotalMedium(totalMedium);
                ranSegment.setTotalHard(totalHard);
                ranSegment.setTotalVeryHard(totalVeryHard);

                ranSegment.setTotalEasyD(totalEasyD);
                ranSegment.setTotalMediumD(totalMediumD);
                ranSegment.setTotalHardD(totalHardD);
                ranSegment.setTotalVeryHardD(totalVeryHardD);

                ranSegment.setNext300(next300);
                ranSegment.setLast300(last300);

                ranSegment.setRanSession(session);

                // Reset segment variables
                totalMedium += totalMediumSegment;
                totalTimeMedium += totalTimeMediumSegment;
                elevationGain += elevationGainSegment;
                totalHard += totalHardSegment;
                totalTimeHard += totalTimeHardSegment;
                totalVeryHard += totalVeryHardSegment;
                totalTimeVeryHard += totalTimeVeryHardSegment;
                totalVeryHardD += totalVeryHardDSegment;
                totalTimeVeryHardD += totalTimeVeryHardDSegment;
                elevationLoss += elevationLossSegment;

                totalHardD += totalHardDSegment;
                totalTimeHardD += totalTimeHardDSegment;
                totalMediumD += totalMediumDSegment;
                totalTimeMediumD += totalTimeMediumDSegment;
                totalEasy += totalEasySegment;
                totalTimeEasy += totalTimeEasySegment;

                // Reset segment
                totalEasySegment = 0.0;
                totalMediumSegment = 0.0;
                totalHardSegment = 0.0;
                totalVeryHardSegment = 0.0;
                totalEasyDSegment = 0.0;
                totalMediumDSegment = 0.0;
                totalHardDSegment = 0.0;
                totalVeryHardDSegment = 0.0;
                totalTimeEasySegment = 0L;
                totalTimeMediumSegment = 0L;
                totalTimeHardSegment = 0L;
                totalTimeVeryHardSegment = 0L;
                totalTimeEasyDSegment = 0L;
                totalTimeMediumDSegment = 0L;
                totalTimeHardDSegment = 0L;
                totalTimeVeryHardDSegment = 0L;
                elevationGainSegment = 0;
                elevationLossSegment = 0;
                next300 = 0;
                last300 = 0;
            } /*else*/ {
                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                if (rGrade >= 2 && rGrade < 10) {
                    //totalMedium += segment.getDistance();
                    //totalTimeMedium += segment.getTime();
                    totalMediumSegment += segment.getDistance();
                    totalTimeMediumSegment += segment.getTime();
                    //elevationGain += segment.getElevation();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade >= 10 && rGrade < 20) {
                    //totalHard += segment.getDistance();
                    //totalTimeHard += segment.getTime();
                    totalHardSegment += segment.getDistance();
                    totalTimeHardSegment += segment.getTime();
                    //elevationGain += segment.getElevation();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade >= 20) {
                    //totalVeryHard += segment.getDistance();
                    //totalTimeVeryHard += segment.getTime();
                    totalVeryHardSegment += segment.getDistance();
                    totalTimeVeryHardSegment += segment.getTime();
                    //elevationGain += segment.getElevation();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade < -20) {
                    //totalVeryHardD += segment.getDistance();
                    //totalTimeVeryHardD += segment.getTime();
                    totalVeryHardDSegment += segment.getDistance();
                    totalTimeVeryHardDSegment += segment.getTime();
                    //elevationLoss += segment.getElevation();
                    elevationLossSegment += segment.getElevation();
                } else if (rGrade >= -20 && rGrade < -10) {
                    //totalHardD += segment.getDistance();
                    //totalTimeHardD += segment.getTime();
                    totalHardDSegment += segment.getDistance();
                    totalTimeHardDSegment += segment.getTime();
                    //elevationLoss += segment.getElevation();
                    elevationLossSegment += segment.getElevation();
                } else if (rGrade >= -10 && rGrade < -2) {
                    //totalMediumD += segment.getDistance();
                    //totalTimeMediumD += segment.getTime();
                    totalMediumDSegment += segment.getDistance();
                    totalTimeMediumDSegment += segment.getTime();
                    //elevationLoss += segment.getElevation();
                    elevationLossSegment += segment.getElevation();
                } else {
                    //totalEasy += segment.getDistance();
                    //totalTimeEasy += segment.getTime();
                    totalEasySegment += segment.getDistance();
                    totalTimeEasySegment += segment.getTime();
                }
                if (totalEasySegment + totalEasyDSegment + totalMediumSegment + totalMediumDSegment + totalHardSegment + totalHardDSegment +
                        + totalVeryHardSegment + totalVeryHardDSegment < 300) {
                    next300 += segment.getElevation();
                } else if (totalEasySegment + totalEasyDSegment + totalMediumSegment + totalMediumDSegment + totalHardSegment + totalHardDSegment +
                        + totalVeryHardSegment + totalVeryHardDSegment > segment.getDistance() -300) {
                    last300 += segment.getElevation();
                }
            }
            // Next
            i++;
        }
        // Did we miss some sections?
        if (segments.size() % nbSegments != 0) {
            RanSegment ranSegment = new RanSegment();
            ranSegments.add(ranSegment);
            // Set the calculated variables
            ranSegment.setTotalEasySegment(totalEasySegment);
            ranSegment.setTotalMediumSegment(totalMediumSegment);
            ranSegment.setTotalHardSegment(totalHardSegment);
            ranSegment.setTotalVeryHardSegment(totalVeryHardSegment);

            ranSegment.setTotalEasyDSegment(totalEasyDSegment);
            ranSegment.setTotalMediumDSegment(totalMediumDSegment);
            ranSegment.setTotalHardDSegment(totalHardDSegment);
            ranSegment.setTotalVeryHardDSegment(totalVeryHardDSegment);

            ranSegment.setTotalTimeEasySegment(totalTimeEasySegment);
            ranSegment.setTotalTimeMediumSegment(totalTimeMediumSegment);
            ranSegment.setTotalTimeHardSegment(totalTimeHardSegment);
            ranSegment.setTotalTimeVeryHardSegment(totalTimeVeryHardSegment);

            ranSegment.setTotalTimeEasyDSegment(totalTimeEasyDSegment);
            ranSegment.setTotalTimeMediumDSegment(totalTimeMediumDSegment);
            ranSegment.setTotalTimeHardDSegment(totalTimeHardDSegment);
            ranSegment.setTotalTimeVeryHardDSegment(totalTimeVeryHardDSegment);

            ranSegment.setTotalTimeEasy(totalTimeEasy);
            ranSegment.setTotalTimeMedium(totalTimeMedium);
            ranSegment.setTotalTimeHard(totalTimeHard);
            ranSegment.setTotalTimeVeryHard(totalTimeVeryHard);

            ranSegment.setTotalTimeEasyD(totalTimeEasyD);
            ranSegment.setTotalTimeMediumD(totalTimeMediumD);
            ranSegment.setTotalTimeHardD(totalTimeHardD);
            ranSegment.setTotalTimeVeryHardD(totalTimeVeryHardD);

            ranSegment.setElevationGain(elevationGain);
            ranSegment.setElevationLoss(elevationLoss);
            ranSegment.setElevationGainSegment(elevationGainSegment);
            ranSegment.setElevationLossSegment(elevationLossSegment);

            ranSegment.setTotalEasy(totalEasy);
            ranSegment.setTotalMedium(totalMedium);
            ranSegment.setTotalHard(totalHard);
            ranSegment.setTotalVeryHard(totalVeryHard);

            ranSegment.setTotalEasyD(totalEasyD);
            ranSegment.setTotalMediumD(totalMediumD);
            ranSegment.setTotalHardD(totalHardD);
            ranSegment.setTotalVeryHardD(totalVeryHardD);

            ranSegment.setRanSession(session);
        }

        session.setTotalEasy(totalEasy);
        session.setTotalMedium(totalMedium);
        session.setTotalHard(totalHard);
        session.setTotalVeryHard(totalVeryHard);
        session.setTotalEasyD(totalEasyD);
        session.setTotalMediumD(totalMediumD);
        session.setTotalHardD(totalHardD);
        session.setTotalVeryHardD(totalVeryHardD);
        session.setElevationGain(elevationGain);

        return ranSegments;
    }

    private void printSegment(Segment segment) {
        System.out.println(segment.getDistance() + "\t"
                + String.format("%.2f", segment.getGrade() * 100) + "\t"
                + segment.getElevation() + "\t"
                + segment.getSpeed());
    }

    private void printRanSegment(RanSegment ranSegment) {
        System.out.println(
                String.format("%.2f", ranSegment.getDistance()) + "\t" +
                        String.format("%.2f", ranSegment.getSpeed()) + "\t" +
                        ranSegment.getNext300() + "\t" +
                        String.format("%5.2f", ranSegment.getTotalEasy())  + "\t" +
                        String.format("%5.2f", ranSegment.getTotalEasySegment()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalMedium()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalMediumSegment()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalHard()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalHardSegment()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalVeryHard()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalVeryHardSegment()) + "\t" +
                ranSegment.getElevationGain() + "\t" +
                        ranSegment.getElevationGainSegment() + "\t" +
                ranSegment.getTotalTimeEasy()  + "\t" +
                        ranSegment.getTotalTimeEasySegment()  + "\t" +
                ranSegment.getTotalTimeMedium() + "\t" +
                        ranSegment.getTotalTimeMediumSegment() + "\t" +
                ranSegment.getTotalTimeHard() + "\t" +
                        ranSegment.getTotalTimeHardSegment() + "\t" +
                ranSegment.getTotalTimeVeryHard() + "\t" +
                        ranSegment.getTotalTimeVeryHardSegment() + "\t" +
                        String.format("%5.2f", ranSegment.getTotalEasyD())  + "\t" +
                        String.format("%5.2f", ranSegment.getTotalEasyDSegment())  + "\t" +
                        String.format("%5.2f", ranSegment.getTotalMediumD()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalMediumDSegment()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalHardD()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalHardDSegment()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalVeryHardD()) + "\t" +
                        String.format("%5.2f", ranSegment.getTotalVeryHardDSegment()) + "\t" +
                ranSegment.getElevationLoss() + "\t" +
                        ranSegment.getElevationLossSegment() + "\t" +
                ranSegment.getTotalTimeEasyD()  + "\t" +
                        ranSegment.getTotalTimeEasyDSegment()  + "\t" +
                ranSegment.getTotalTimeMediumD() + "\t" +
                        ranSegment.getTotalTimeMediumDSegment() + "\t" +
                ranSegment.getTotalTimeHardD() + "\t" +
                        ranSegment.getTotalTimeHardDSegment() + "\t" +
                ranSegment.getTotalTimeVeryHardD() + "\t" +
                        ranSegment.getTotalTimeVeryHardDSegment()
        );
    }

}
