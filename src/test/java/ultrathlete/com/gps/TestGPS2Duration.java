package ultrathlete.com.gps;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;
import ultrathlete.com.data.stats.Utils.MultiplePoint;
import ultrathlete.com.data.stats.linearRegression.MultipleLinearRegression;
import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestGPS2Duration {

    public static void main(String[] args) {

        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
    }


    @Test
    public void readFromMultipleTypeAll2() {

               /*
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
                "C:\\Users\\gille\\Downloads\\cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220618-Montagne-Cova-Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20210821_belier_montagne_7342591908.gpx",
                "C:\\Users\\gille\\Downloads\\challenge-lac-montagne-s.gpx",
                "C:\\Users\\gille\\Downloads\\montagne-leforchat-lescretes-20220724.gpx",
                "C:\\Users\\gille\\Downloads\\20220827_Montagne_ColdeCou_9489574596.gpx",
                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                "C:\\Users\\gille\\Downloads\\plaine7.gpx",
//            "C:\\Users\\gille\\Downloads\\ultra.gpx",
                "C:\\Users\\gille\\Downloads\\orcier.gpx",
                "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
                "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
                "C:\\Users\\gille\\Downloads\\long1.gpx",
                "C:\\Users\\gille\\Downloads\\allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\cfer.gpx",
//                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",
//                "C:\\Users\\gille\\Downloads\\20220423_MontForchat.gpx",
                "C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx",
                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx"      // Ballaison
//                "C:\\Users\\gille\\Downloads\\20220626_sortiedraillant_hilly_bonnesmontees_9090691733.gpx",
//                "C:\\Users\\gille\\Downloads\\lestempliers2022_activity_9845878137.gpx",
//
//                "C:\\Users\\gille\\Downloads\\activity_20231.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20232.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20233.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20234.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20235.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20236.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20237.gpx",
//                "C:\\Users\\gille\\Downloads\\activity_20238.gpx"
*/
/*
    ROSS
                "C:\\Users\\gille\\Downloads\\ross1.gpx",
                "C:\\Users\\gille\\Downloads\\ross2.gpx",
                "C:\\Users\\gille\\Downloads\\ross3.gpx",
                "C:\\Users\\gille\\Downloads\\ross4.gpx",
                "C:\\Users\\gille\\Downloads\\ross5.gpx",
//                "C:\\Users\\gille\\Downloads\\ross100m.gpx",
                "C:\\Users\\gille\\Downloads\\rossCCC_2023.gpx"*/

/*  PAM
                "C:\\Users\\gille\\Downloads\\pam\\2019-11-30.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2022-03-19.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-02-20.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-03-05.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-04-23.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-05-01.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-05-29.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-06-03-80k.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-06-24.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-06-30.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-08-13.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-08-26.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-08-30.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-09-08-100k.gpx",
                "C:\\Users\\gille\\Downloads\\pam\\2023-09-24.gpx"*/
 //       };
/*
                final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\martin\\martin1.gpx",
                "C:\\Users\\gille\\Downloads\\martin\\martin2.gpx",
                "C:\\Users\\gille\\Downloads\\martin\\martin3.gpx",
                "C:\\Users\\gille\\Downloads\\martin\\martin4.gpx",
                "C:\\Users\\gille\\Downloads\\martin\\martin5.gpx",
                "C:\\Users\\gille\\Downloads\\martin\\martin6.gpx"
        };
        */

        // Sarah Sawyer
        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-06-03.gpx",     // 237
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-10-08.gpx",     // 40
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-10-15.gpx",     // 25
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-10-29.gpx",     // 37
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-11-05.gpx",     // 40
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-11-12.gpx",     // 45K
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-11-19.gpx",     // 42K
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-12-03.gpx"      // 160K
        };


        for (int l = 100; l <= 100; l += 100) {      // 100, par 100
            for (int nbS = 5; nbS <= 5; nbS+=5) { // 10 par 10
                System.out.print("\n" + l + "; " + nbS + "; ");

                List<RanSegment> allRanSegments = new ArrayList<>();
                for (int g = 0; g < gpxs.length; g++) {
                    // Segments
                    List<Segment> segments = getSegments(gpxs[g], l);
                    Boolean isRace = false;
                    List<RanSegment> ranSegments = getRanSegments(segments, nbS, isRace);
                    allRanSegments.addAll(ranSegments);

                    //System.out.println("-->" + gpxs[g]);
                }
                calculateRegression(allRanSegments);
            }
        }
    }

    private void calculateRegression(List<RanSegment> ranSegments) {

        //System.out.println("--- Regression ---");

        int j = 0;
        double[][] xpoints1 = new double[ranSegments.size()][8];
        double[] ypoints1 = new double[ranSegments.size()];
        double[][] xpoints2 = new double[ranSegments.size()][4];
        double[][] xpoints2OLS = new double[ranSegments.size()][4];
        double[] ypoints2 = new double[ranSegments.size()];
        double[][] xpoints3 = new double[ranSegments.size()][8];
        double[][] xpoints3OLS = new double[ranSegments.size()][6];
        double[] ypoints3 = new double[ranSegments.size()];
        double[][] xpoints4 = new double[ranSegments.size()][2];
        double[] ypoints4 = new double[ranSegments.size()];

        double[][] xpoints5 = new double[ranSegments.size()][5];
        double[] ypoints5 = new double[ranSegments.size()];

        for (RanSegment segment : ranSegments) {
            xpoints1[j][0] = 1;
            xpoints2[j][0] = 1;
            xpoints3[j][0] = 1;
            xpoints4[j][0] = 1;
            xpoints5[j][0] = 1;


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

            int elevHard = segment.getNext() != null ? segment.getNext().getTotalHardSegment() > 0 ? 1 : 0 : 0;
            int elevVHard = segment.getNext() != null ? segment.getNext().getTotalVeryHardSegment() > 0 ? 2 : 0 : 0;

            // 1, 2 et 3 très bons pour la montagne
            // 1, 2 et 3 parfaits pour la plaine. Avec 3 proche de 100
            // 1, 2 et 3 parfaits pour du varié
            // 1, 2, 5 parfaits pour ltout.
            xpoints1[j][1] = segment.getTotalDistance();
            xpoints1[j][2] = segment.getElevationGainMedium();
            xpoints1[j][3] = segment.getElevationGainHard();
            xpoints1[j][4] = segment.getElevationGainVeryHard();
            xpoints1[j][5] = segment.getElevationLossMedium();
            xpoints1[j][6] = segment.getElevationLossHard();
            xpoints1[j][7] = segment.getElevationLossVeryHard();

            ypoints1[j] =  segment.getTotalTime();
            //System.out.println(segment.getTotalEasy()+","+ segment.getTotalMedium()+","+segment.getTotalHard()+","+segment.getTotalTime());


// Data to regression            System.out.println(segment.getTotalTime() + ": " + segment.getTotalDistance() + ", " + segment.getElevationGain() + ", " + segment.getElevationLoss());

            // Le plus fort en montagne
            // les deux forts en valloné
            xpoints2[j][1] = segment.getTotalDistance();
            xpoints2[j][2] = segment.getElevationGain();
            xpoints2[j][3] = segment.getElevationLoss();

            ypoints2[j] =  segment.getTotalTime();

            // Plus fort en plaine. Mais les deux à 99
            xpoints3[j][1] = segment.getTotalEasy();
            xpoints3[j][2] = segment.getTotalMedium();
            xpoints3[j][3] = segment.getTotalHard();
//            xpoints3[j][4] = segment.getTotalVeryHard();
            xpoints3[j][4] = segment.getTotalMediumD();
            xpoints3[j][5] = segment.getTotalHardD();
            xpoints3[j][6] = segment.getTotalVeryHard();
            xpoints3[j][7] = segment.getTotalVeryHardD();

            ypoints3[j] =  segment.getTotalTime();

            xpoints4[j][1] = segment.getTotalDistance();
            ypoints4[j] =  segment.getTotalTime();

            Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
            xpoints5[j][1] = segment.getTotalDistance();
            xpoints5[j][2] = segment.getElevationGain();
            xpoints5[j][3] = segment.getElevationLoss();
            xpoints5[j][4] = rGrade * 100;
            ypoints5[j] =  segment.getTime();

            j++;
        }

        if (j > 0) {

            String reg1 = null, reg2 = null, reg3 = null, reg4 = null, reg5 = null;

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                System.out.print("R1: ");
                if (regression1.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression1.R2() * 100) + "; ");
                reg1 = regression1.beta(0) + " + " + regression1.beta(1) + "up + "
                        + regression1.beta(2) + "down ";

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
                System.out.print(" R2: ");
                if (regression2.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression2.R2() * 100) + "; ");

                reg2 = regression2.beta(0) + " + " + regression2.beta(1) + "distance + "
                        + regression2.beta(2) + "gain + " + regression2.beta(3) + "loss";

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints3, ypoints3);
                System.out.print(" R3: ");
                if (regression3.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression3.R2() * 100) + "; ");

                reg3 = regression3.beta(0) + " + " + regression3.beta(1) + "easy + "
                        + regression3.beta(2) + "medium + " + regression3.beta(3) + "hard" +
                        + regression3.beta(4) + "mediumD + " + regression3.beta(5) + "hardD + "
                        + regression3.beta(6) + "Vhard + "  + regression3.beta(7) + "VhardD";

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints4, ypoints4);
                System.out.print(" R4: ");
                if (regression4.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression4.R2() * 100) + "; ");
                reg4 = regression4.beta(0) + " + " + regression4.beta(1) + "distance";
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression5 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints5, ypoints5);
                System.out.print(" R5: ");
                if (regression5.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression5.R2() * 100) + "; ");

                reg5 = regression5.beta(0) + " + " + regression5.beta(1) + "distance + "
                        + regression5.beta(2) + "gain + " + regression5.beta(3) + "loss" +
                        +regression5.beta(4) + "grade";

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            System.out.println();
            System.out.println("R1: " + reg1);
            System.out.println("R2: " + reg2);
            System.out.println("R3: " + reg3);
            System.out.println("R4: " + reg4);
            System.out.println("R5: " + reg5);

            // Essai
//            System.out.println("x1-----------------------------------------------------");
//            for (int i = 0; i < ypoints1.length; i++) {
//                System.out.println(ypoints1[i] + ";" + xpoints1[i][0] + ";" + xpoints1[i][1]  + ";" + xpoints1[i][2]
//                        + ";" + xpoints1[i][3]  + ";" + xpoints1[i][4]  + ";" + xpoints1[i][5]  + ";" + xpoints1[i][6]
//                        + ";" + xpoints1[i][7]);
//            }
//

            System.out.println("x2-----------------------------------------------------");
            for (int i = 0; i < ypoints2.length; i++) {
                System.out.println(ypoints2[i] + ";" + xpoints2[i][0] + ";" + xpoints2[i][1]  + ";" + xpoints2[i][2]
                        + ";" + xpoints2[i][3]);
            }

            System.out.println("x3-----------------------------------------------------");
            for (int i = 0; i < ypoints3.length; i++) {
                System.out.println(ypoints3[i] + ";" + xpoints3[i][0] + ";" + xpoints3[i][1]  + ";" + xpoints3[i][2]
                        + ";" + xpoints3[i][3]+ ";" + xpoints3[i][4] + ";" + xpoints3[i][5] + ";" + xpoints3[i][6]
                        + ";" + xpoints3[i][7]);
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
                    int lastElevation = 0;
                    int lastSegmentElevation = 0;

                    double segmentDistance = 0.0;
                    WayPoint startSegment = null;
                    WayPoint endSegment = null;

                    WayPoint lastPoint = null;
                    WayPoint lastSegment = null;
                    Length distance = null;

                    for (WayPoint point : list) {

                        lastElevation = point.getElevation().isPresent() ? point.getElevation().get().intValue() : lastElevation;
                        lastPoint = point;
                        if (startSegment == null) {
                            startSegment = point;
                        }
                        if (i++ > 0) {
                            distance = Geoid.WGS84.distance(prev, point);
                            //System.out.println("Distance: " + distance + " at " + point.getTime());

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
                                } else {
                                    segment.setElevationFrom(lastSegmentElevation);
                                }
                                if (endSegment.getElevation().isPresent()) {
                                    segment.setElevationTo(endSegment.getElevation().get().intValue());
                                    lastSegmentElevation = endSegment.getElevation().get().intValue();
                                } else {
                                    segment.setElevationTo(lastElevation);
                                    lastSegmentElevation = lastElevation;
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
                        }  else {
                            segment.setElevationFrom(lastSegmentElevation);
                        }
                        if (prev.getElevation().isPresent()) {
                            segment.setElevationTo(prev.getElevation().get().intValue());
                        } else {
                            segment.setElevationTo(lastElevation);
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

    @Test
    public void predict() {

        final String[] gpxs = {
//               "C:\\Users\\gille\\Downloads\\wild50k_parcours_officiel_e741372209.gpx",
//                "C:\\Users\\gille\\Downloads\\grand_trail_des_templiers_171094.gpx",
//                "C:\\Users\\gille\\Downloads\\lestempliers.gpx"
//                "C:\\Users\\gille\\Downloads\\trace-du-trail-ccc-courmayeur-champex-chamonix.gpx",
//               "C:\\Users\\gille\\Downloads\\CCC_2023.gpx",
//                "C:\\Users\\gille\\Downloads\\West_Highland_Way_Race_2019.gpx"
//                "C:\\Users\\gille\\Downloads\\CCC_2023_bis.gpx",
//                "C:\\Users\\gille\\Downloads\\rossCCC_2023.gpx"
//                "C:\\Users\\gille\\Downloads\\ross100m.gpx"
//                "C:\\Users\\gille\\Downloads\\pam\\2023-06-03-80k.gpx",
//               "C:\\Users\\gille\\Downloads\\pam\\2023-09-08-100k.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\DiagonaleDesFous2023.gpx"
//                "C:\\Users\\gille\\Downloads\\hivernatedestempliers.gpx",
//                "C:\\Users\\gille\\Downloads\\sparnatrail.gpx",
//                "C:\\Users\\gille\\Downloads\\ptiot_tracedetrail_226067.gpx",
//                "C:\\Users\\gille\\Downloads\\ptiotsparnatrail.gpx"
//                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2022-12-03.gpx"
                "C:\\Users\\gille\\Downloads\\SarahSawyer\\2023-04-22.gpx"
        };


        for (int l = 100; l <= 100; l += 100) {      // 100, par 100
            for (int nbS = 5; nbS <= 5; nbS+=5) { // 10 par 10
                System.out.print("\n" + l + "; " + nbS + "; ");

                for (int g = 0; g < gpxs.length; g++) {
                    // Segments
                    System.out.println(gpxs[g]);
                    List<Segment> segments = getSegments(gpxs[g], l);
                    List<RanSegment> ranSegments = getRanSegments(segments, nbS, false);
                    predictRun(ranSegments, gpxs[g]);
                }
            }
        }

        // Gilles
//        100; 5; ***95,18; ***98,26; ***97,90; ***90,80;
//        -268.5715017877468 + 0.44806633235904136easy + 0.7320692428140524medium + 1.8217515094808039hard
//                -357.8396287730519 + 0.3216431232694592distance + 3.22251035216564gain + -1.8061784068402824loss
//                -135.9541404448948 + 0.2920654632697146easy + 0.5319590280882981medium + 1.1874294265186975hard0.26603038924053984mediumD + 0.7346353959083033hardD

        // Gilles avec Templiers
//        100; 5; ***98,90; ***98,02; ***98,85; ***95,24;
//        -758.5871949800869 + 0.2489305711828353up + 1.6370605648022865down
//                -1379.860852862271 + 0.3720179101863347distance + 3.7098177833825923gain + -2.576310867287926loss
//                -767.9084270418372 + 0.36780534598722603easy + 0.29952412920732707medium + 0.41036032085767044hard0.4120407154727509mediumD + 2.077526227343742hardD2.239755740965526VhardD0.4009627166076757VhardD

        // Gilles avec templiers et 2023
//        100; 5; ***98,54; ***97,94; ***98,55; ***94,25;
//        -789.4754010589837 + 0.31521088308907963up + 0.4928931993909686down
//                -1265.5131275274236 + 0.3656093362450811distance + 3.874262626462944gain + -2.449242598380254loss
//                -790.2068275029933 + 0.46544879937797984easy + 0.2907420581635194medium + 0.5441439649098528hard0.38861505736305185mediumD + 1.6923434310744725hardD2.2735984553433775VhardD0.5060802916027101VhardD

        // Gilles 2023
//        100; 5; ***99,51; ***99,15; ***99,38; 80,53;
//        -84.09710323048071 + 0.3498974118122892up + 5.102320805400997down
//        56.24650874232405 + 0.2693383189332736distance + 4.005689458237696gain + -2.5528679702173944loss
//                -101.37225951428466 + 0.32640938187290425easy + 0.6099380072441192medium + 0.7440560983572857hard0.2767973565370857mediumD + 0.13700157402622898hardD1.5672016550287602VhardD0.7923587597230402VhardD
//
        // Gilles Tout
//        100; 5; R1: ***98,54;  R2: ***97,94;  R3: ***98,55;  R4: ***94,25;
//        R1: -789.4754010589837 + 0.31521088308907963up + 0.4928931993909686down
//        R2: -1265.5131275274236 + 0.3656093362450811distance + 3.874262626462944gain + -2.449242598380254loss
//        R3: -790.2068275029933 + 0.46544879937797984easy + 0.2907420581635194medium + 0.5441439649098528hard0.38861505736305185mediumD + 1.6923434310744725hardD + 2.2735984553433775Vhard + 0.5060802916027101VhardD
//        R4: -1152.3846411889863 + 0.5865275793910271distance


        // Alice
//        -549.8552316635556 + 0.45326415075985327easy + 1.1307274043675313medium + 0.8983640795888473hard
//                -482.69586870818915 + 0.46254591713329857distance + 6.784978353858557gain + 5.6883784221961loss
//                -654.8269125217936 + 0.5275703697455396easy + 1.3008991094172053medium + -0.5163523739781466hard-0.25870162726008mediumD + 1.245848458339393hardD

        // Alice 2
//        -1203.0353786101148 + 0.5034924984148distance + 2.4581513707099103gain + 2.2658704685018436loss
//                -1161.3473292034485 + 0.46848751613815537easy + 0.6459895450413217medium + 0.8034688525342495hard0.4360079351238415mediumD + 0.2565877932782695hardD
    }

    private void predictRun(List<RanSegment> ranSegments, String name) {
        for (RanSegment ran : ranSegments) {
            long time = ran.getTotalTime();
            double distance = ran.getTotalDistance();
            long gain = ran.getElevationGain();
            long loss = ran.getElevationLoss();
            double easy = ran.getTotalEasy();
            double medium = ran.getTotalMedium();
            double hard = ran.getTotalHard();
            double mediumD = ran.getTotalMediumD();
            double hardD = ran.getTotalHardD();
            double vHardD = ran.getTotalVeryHardD();
            double vHard = ran.getTotalVeryHard();



// Gilles Tout
            //100; 5; R1: ***98,54;  R2: ***97,94;  R3: ***98,55;  R4: ***94,25;
//            double predict1 = -789.4754010589837 + 0.31521088308907963 * gain + 0.4928931993909686 * loss;
//            double predict2 = -1265.5131275274236 + 0.3656093362450811 * distance + 3.874262626462944 * gain + -2.449242598380254 * loss;
//            double predict3 = -790.2068275029933 + 0.46544879937797984 * easy + 0.2907420581635194 * medium + 0.5441439649098528 * hard
//                                + 0.38861505736305185 * mediumD + 1.6923434310744725 * hardD + 2.2735984553433775 * vHard + 0.5060802916027101 * vHardD;
//            double predict4 = -1152.3846411889863 + 0.5865275793910271 * distance;

// Gilles Plaine
//            double predict1 = -135.27606563888074 + 0.3186912389045695 * gain + 0.7937050018852801 * loss;
//            double predict2 = -155.47622153362485 + 0.3019017774667627 * distance + 2.602016802211377 * gain + -2.225739214266199 * loss;
//            double predict3 = -156.09603196839362 + 0.15163015500540164 * easy + 0.407100456894569 * medium
//                            + 0.8051204093362434 * hard + 0.4559092984103884 * mediumD + 0.04845591043111672 * hardD
//                            + 1.242407971477999 * vHard + 0.8813182783450934 * vHardD;
//            double predict4 = -115.28469368434543 + 0.39364249544043245 * distance;

            // Ross avant Lakeland
//            double predict1 = -398.1726481210423 + 0.36086480180673297 * gain + 6.5264486130260755 * loss;
//            double predict2 =  -451.15450730169636 + 0.4135813465960327 * distance + 3.060980388134926 * gain + -1.0438961615472702 *loss;
//            double predict3 =  -366.61223843808676 + 0.30683531314012064 * easy + 0.49246026968786716 * medium + 0.9683957280099376 * hard
//                                + 0.4840175043972678 * mediumD + 1.8198578127781397 * hardD + 0.31948689061715807 * vHard
//                                + 0.5501490791056944 * vHardD;
//            double predict4 =  237.2089433439279 + -7.315691786162684E-4 * distance;

            // Ross long runs + CCC
            //100; 5; R1: ***99,78;  R2: ***99,31;  R3: ***99,69;  R4: 16,43; Matrix is rank deficient.;
//            double predict1 = -1290.0699512006659 + 0.47356247059225 * gain + 19.299011633244714 * loss;
//            double predict2 = -1770.881044669037 + 0.43180032144106434 * distance + 2.559379458844569 * gain + -4.4812941550722485 * loss;
//            double predict3 = -1660.2032250977004 + 0.6003148859298459 * easy + 1.4020085427239797 * medium + 0.6692835552473417 * hard
//                                -0.18367650937361496 * mediumD + 0.38397634689731713 * hardD + 1.109021149969124 + vHard
//                                + 1.7715022101318194 * vHardD;
//            double predict4 = 201.9587178408362 + 0.00329367238359314 * distance;

    // ROSS Avant la CCC
//            100; 5; R1: ***99,73;  R2: ***99,28;  R3: ***99,73;  R4: ***98,89;
//            double predict1 = -1287.4790805215991 + 0.4204454492218897 * gain + 8.94313316732307 * loss;
//            double predict2 =  -2521.9817821896086 + 0.46257258091337977 * distance + -8.591920145307647 * gain + -15.548388866475237 * loss;
//            double predict3 =  -942.1500433686193 + 0.5380298671562942 * easy + 0.6510403876699412 * medium + -0.7624197478520052 * hard
//                                + 0.002104220960229656 * mediumD + 5.8195315183750145 * hardD + 1.352812724624886 * vHard
//                                + 0.4698485341700693 * vHardD;
//            double predict4 =  -4408.592339325922 + 0.713408435144408 * distance;

//            // Ross après la CCC
//            100; 5; R1: ***99,35;  R2: ***99,11;  R3: ***99,32;  R4: ***97,60;
/*
            double predict1 = -1807.025975655727 + 0.2318989315519473 * gain + 21.06890183421219 * loss;
            double predict2 =  -3057.6045675059977 + 0.47261008451164815 * distance + 1.5929355608708085 * gain + -5.009661909792799 * loss;
            double predict3 =  -2231.4126090638415 + 0.43339305469148043 * easy + 1.2108571279946994 * medium + -0.29624061497369264 * hard
                                + 0.05891249001000891 * mediumD + 3.0992326575874234 * hardD + 2.077484594558934 * vHard
                                + 0.31553558822641264 * vHardD;
            double predict4 =  -3092.951689830851 + 0.7241390981119462 * distance;
*/
            // Pam avant le 80
//            double predict1 = 25.81636146832081 + 0.3837443728319824 * gain + -0.5339773189943466 * loss;
//            double predict2 =  -56.8610029679584 + 0.3953022053209045 * distance + -0.4782166055556954 * gain + 1.5131579795372605 *loss;
//            double predict3 =  73.98580759579768 + 0.39996234841208295 * easy + 0.3471120864859813 * medium + 0.8919538162201575 * hard
//                                + 0.33961761694236486 * mediumD + -0.11735172278796807 * hardD + -3.132749522813921 * vHard
//                                + 1.4699621574576434 * vHardD;
//            double predict4 =  -214.35051589586686 + 0.37904582952316834 * distance;

            // Pam avant le 100k
//            double predict1 = -1122.4665934093446 + 0.4515990384850929 * gain + -1.763397092431126 * loss;
//            double predict2 =  -1617.281221319553 + 0.4525739445576232 * distance + -0.31057810855012263 * gain + -2.603572951029606 * loss;
//            double predict3 =  -1127.9542309359995 + 0.37435551390179583 * easy + 0.42092228147063004 * medium + 0.44559519928126035 * hard
//                                + 0.60744938493035 * mediumD + -0.19307490592922777 * hardD + -0.40961019102668705 * vHard
//                                + 1.867838596582498 * vHardD;
//            double predict4 =  -1956.0152471631495 + 0.5191607952303846 * distance;

            // Pam tout sauf les courses
//            100; 5; R1: ***98,18;  R2: ***96,93;  R3: ***98,26;  R4: ***93,87;
//            double predict1 = -929.6233251752919 + 0.4543450087345109 * gain + -1.1440126427818829 * loss;
//            double predict2 =  -1624.7754180865231 + 0.43664860849591336 * distance + 2.1363353675165038 * gain + -2.644311444796987 * loss;
//            double predict3 = -954.4871443048908 + 0.3736941199018833 * easy + 0.42789107220613504 * medium + 0.0243754287158347 *hard
//                                + 0.6006003075432051 * mediumD + -0.18413321612957192 * hardD + 2.062408985036759 * vHard
//                                + 0.8292549708621492 * vHardD;
//            double predict4 =  -826.5193955692403 + 0.45657216630034914 * distance;

            // Pam tout sauf le 100
            //100; 5; R1: ***98,97;  R2: ***98,33;  R3: ***98,99;  R4: ***96,62;
//            double predict1 = -1177.3921169827308 + 0.4403521199073246 * gain + 0.8920045701008193 * loss;
//            double predict2 =  -1293.8228151943347 + 0.44171318917202407 * distance + 1.5365527019119476 * gain + -0.802531655112195 * loss;
//            double predict3 = -1170.2946634929763 + 0.36903268940591893 * easy + 0.5171560043620069 * medium + -0.0509577177450789 * hard
//                                + 0.5813116671064279 * mediumD + -0.059779783963825425 * hardD + 1.6259700312138576 * vHard
//                                + 1.3781817636601454 * vHardD;
//            double predict4 =  -1500.3807573210288 + 0.512406970487659 * distance;

            // Pam total
//            double predict1 = -768.7506480414625 + 0.46594476109567395 * gain + -3.6570810210390623 * loss;
//            double predict2 = -1155.9042796059111 + 0.3201031732674322 * distance + 1.3022640652849529 * gain + -5.9858343003936705 * loss;
//            double predict3 = -887.2833808797211 + 0.5073794446776924 * easy + 0.33822462062917447 * medium + 0.7729094802539171 * hard
//                            + 0.39207274639739037 * mediumD + -0.7282965777796309 * hardD + 0.7045151881892853 * vHard
//                            + 2.0125182257921006 * vHardD;
//            double predict4 = 179.1832335287378 + 0.0020466166677509633 * distance;

            // Martin
//            -176.9930821986953 + 0.39737299869634674 * distance + 0.5875778951452396gain + 0.9073318800675263loss
//            250.38157386570973 + 0.15568276569097234easy + 0.5403055090861627medium + 0.09355007428794343hard0.25943442849956827mediumD + 1.767464260752412hardD0.47579073367944663VhardD-0.1962803818751359VhardD

//            double predict2 = -176.9930821986953 + 0.39737299869634674 * distance + 0.5875778951452396 * gain + 0.9073318800675263 * loss;
//            double predict3 = 250.38157386570973 + 0.15568276569097234 * easy + 0.5403055090861627 * medium
//                    + 0.09355007428794343 * hard + 0.25943442849956827 * mediumD + 1.767464260752412 * hardD
//                    + 0.47579073367944663 * VhardD -0.1962803818751359 * VhardD;

                    // Alice

//            double predict2 = -482.69586870818915 + (0.46254591713329857 * distance) + (6.784978353858557 * gain)
//                    + (5.6883784221961 * loss);
//            double predict3 = -654.8269125217936 + (0.5275703697455396 * easy) + (1.3008991094172053 * medium)
//                    + -(0.5163523739781466 * hard) - (0.25870162726008 * mediumD) + (1.245848458339393 * hardD);

            // Alice 2
//            double predict2 = -1203.0353786101148 + (0.5034924984148 * distance) + (2.4581513707099103 * gain)
//                    + (2.2658704685018436 * loss);
//            double predict3 = -689.9658761225232 + (0.5911089583443365 * easy) + (0.8847569972946396 * medium)
//                    + (0.8952628576401804 * hard) + (0.19967506027049167 * mediumD) - (1.229527692109539* hardD)
//                    + (3.5710652223019466 * vhard) + (0.509712318021855 * VhardD);

            // Sarah Sawyer (sans inclure Daytona 100 (normal prédiction)
//            double predict1 = 0.0;
//            double predict2 = 470.50189053331104 + 0.557836620438371 * distance + -45.204921471894934 * gain + 41.74818803675545 * loss;
//            double predict3 = 0.0;
//            double predict4 = -2449.5250083813835 + 0.4359657158169106 * distance;
            // Sarah Sawyer en incluant Daytona 100
            // Très bon. Ce qui veut dire qu'il faut les points de la distance
            // --> Essai avec la track 100. Dès que Daytona 100 est dedans, plus grande précision. Changement complet de predict2
            // Devenant précis
            double predict1 = 0.0;
            double predict2 = -4090.311835699435 + 0.42125602525659445 * distance + -32.02882511196349 * gain + -31.033853942257185 * loss;
            double predict3 = 0.0;
            double predict4 = -2592.6323571845264 + 0.4286023534748855 * distance;


                    predict1 = predict1 > 0 ? predict1 : 0.0;
            predict2 = predict2 > 0 ? predict2 : 0.0;
            predict3 = predict3 > 0 ? predict3 : 0.0;
            predict4 = predict4 > 0 ? predict4 : 0.0;

            System.out.println(distance + "\t(" + gain + ")\t" + "\t[" + loss + "]\t" + makeReadable(time)  + "\t"
                    + "\t" + makeReadable((long) predict1) + "\t" + makeReadable((long) predict2)
                    + "\t" + makeReadable((long) predict3) + "\t" + makeReadable((long) predict4)
            );
        }
        System.out.println(name);
        System.out.println("------------------------------------------------------------------------------------");
    }

    private List<RanSegment> getRanSegments(List<Segment> segments, int nbSegments, boolean isRace) {
        List<RanSegment> ranSegments = new ArrayList<>();

        int segmentType;
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

        Integer elevationGainMedium = 0;
        Integer elevationGainHard = 0;
        Integer elevationGainVeryHard = 0;
        Integer elevationLossMedium = 0;
        Integer elevationLossHard = 0;
        Integer elevationLossVeryHard = 0;

        Integer elevationGainMediumSegment = 0;
        Integer elevationGainHardSegment = 0;
        Integer elevationGainVeryHardSegment = 0;
        Integer elevationLossMediumSegment = 0;
        Integer elevationLossHardSegment = 0;
        Integer elevationLossVeryHardSegment = 0;

        int i = 0;
        RanSegment previous = null;
        RanSession session = new RanSession();
        session.setRace(isRace);

        for (Segment segment : segments) {

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

                elevationGainMedium += elevationGainMediumSegment;
                elevationGainHard += elevationGainHardSegment;
                elevationGainVeryHard += elevationGainVeryHardSegment;
                elevationLossMedium += elevationLossMediumSegment;
                elevationLossHard += elevationLossHardSegment;
                elevationLossVeryHard += elevationLossVeryHardSegment;

                ranSegment.setElevationGainHard(elevationGainHard);
                ranSegment.setElevationGainVeryHard(elevationGainVeryHard);
                ranSegment.setElevationGainMedium(elevationGainMedium);
                ranSegment.setElevationLossHard(elevationLossHard);
                ranSegment.setElevationLossVeryHard(elevationLossVeryHard);
                ranSegment.setElevationLossMedium(elevationLossMedium);

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
                elevationGainMediumSegment = 0;
                elevationGainHardSegment = 0;
                elevationGainVeryHardSegment = 0;
                elevationLossMediumSegment = 0;
                elevationLossHardSegment = 0;
                elevationLossVeryHardSegment = 0;
                next300 = 0;
                last300 = 0;
            } //else {
                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                if (rGrade >= 2 && rGrade < 10) {                       // 2 -> 4
                    totalMediumSegment += segment.getDistance();
                    totalTimeMediumSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationGainSegment += segment.getElevation();
                    elevationGainMediumSegment += segment.getElevation();
                } else if (rGrade >= 10 && rGrade < 20) {
                    totalHardSegment += segment.getDistance();
                    totalTimeHardSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationGainSegment += segment.getElevation();
                    elevationGainHardSegment += segment.getElevation();
                } else if (rGrade >= 20) {
                    totalVeryHardSegment += segment.getDistance();
                    totalTimeVeryHardSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationGainSegment += segment.getElevation();
                    elevationGainVeryHardSegment += segment.getElevation();
                } else if (rGrade <= -20) {
                    totalVeryHardDSegment += segment.getDistance();
                    totalTimeVeryHardDSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationLossSegment += segment.getElevation();
                    elevationLossVeryHardSegment += segment.getElevation();
                } else if (rGrade > -20 && rGrade <= -10) {
                    totalHardDSegment += segment.getDistance();
                    totalTimeHardDSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationLossSegment += segment.getElevation();
                    elevationLossHardSegment += segment.getElevation();
                } else if (rGrade > -10 && rGrade <= -2) {
                    totalMediumDSegment += segment.getDistance();
                    totalTimeMediumDSegment += segment.getTime() != null ? segment.getTime() : 0;
                    elevationLossSegment += segment.getElevation();
                    elevationLossMediumSegment += segment.getElevation();
                } else {
                    totalEasySegment += segment.getDistance();
                    totalTimeEasySegment += segment.getTime() != null ? segment.getTime() : 0;
                }
           // }
            // Next
            i++;
        }
        // Did we miss some sections?
        if (segments.size() % nbSegments != 0) {
            RanSegment ranSegment = new RanSegment();
            ranSegments.add(ranSegment);
            // Set the calculated variables
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

            elevationGainMedium += elevationGainMediumSegment;
            elevationGainHard += elevationGainHardSegment;
            elevationGainVeryHard += elevationGainVeryHardSegment;
            elevationLossMedium += elevationLossMediumSegment;
            elevationLossHard += elevationLossHardSegment;
            elevationLossVeryHard += elevationLossVeryHardSegment;

            ranSegment.setElevationGainHard(elevationGainHard);
            ranSegment.setElevationGainVeryHard(elevationGainVeryHard);
            ranSegment.setElevationGainMedium(elevationGainMedium);
            ranSegment.setElevationLossHard(elevationLossHard);
            ranSegment.setElevationLossVeryHard(elevationLossVeryHard);
            ranSegment.setElevationLossMedium(elevationLossMedium);

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

            //System.out.println("-->" + ranSegment.getTotalDistance() + " " + makeReadable(ranSegment.getTotalTime()));
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

    public static String makeReadable(long seconds) {

        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));

    }
}
