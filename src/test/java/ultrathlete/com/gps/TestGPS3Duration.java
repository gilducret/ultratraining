package ultrathlete.com.gps;

import io.jenetics.jpx.*;
import io.jenetics.jpx.geom.Geoid;
import org.junit.Test;
import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TestGPS3Duration {

    public static void main(String[] args) {

        final Point start = WayPoint.of(47.2692124, 11.4041024);
        final Point end = WayPoint.of(47.3502, 11.70584);
        final Length distance = Geoid.WGS84.distance(start, end);
        System.out.println(distance);
    }


    @Test
    public void readFromMultipleTypeAll2() {

//        final String[] gpxs = {
//                "C:\\Users\\gille\\Downloads\\pj1.gpx",
//                "C:\\Users\\gille\\Downloads\\pj2.gpx",
//                "C:\\Users\\gille\\Downloads\\pj3.gpx",
//                "C:\\Users\\gille\\Downloads\\pj4.gpx",
//                "C:\\Users\\gille\\Downloads\\pj5.gpx",
//                "C:\\Users\\gille\\Downloads\\pj6.gpx",
//                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
//                "C:\\Users\\gille\\Downloads\\m1.gpx",
//                "C:\\Users\\gille\\Downloads\\m2.gpx",
//                "C:\\Users\\gille\\Downloads\\m3.gpx",
//                "C:\\Users\\gille\\Downloads\\m4.gpx",
//                "C:\\Users\\gille\\Downloads\\m5.gpx",
//                "C:\\Users\\gille\\Downloads\\m6.gpx",
//                "C:\\Users\\gille\\Downloads\\m7.gpx",
//                "C:\\Users\\gille\\Downloads\\cretes.gpx",
//                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
//
//                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine7.gpx",
//
//            "C:\\Users\\gille\\Downloads\\ultra.gpx",
//                "C:\\Users\\gille\\Downloads\\orcier.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
//                "C:\\Users\\gille\\Downloads\\long1.gpx",
//                "C:\\Users\\gille\\Downloads\\allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\cfer.gpx",
//                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
//                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",
//                "C:\\Users\\gille\\Downloads\\20220423_MontForchat.gpx",
//                "C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx"      // Ballaison
//
//        };


        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\Alice\\Alice1-145.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice2.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice3.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice4.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice5.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice6.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice7.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice8.gpx",
                "C:\\Users\\gille\\Downloads\\Alice\\Alice9.gpx"
        };

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

        for (int l = 10; l <= 100; l += 100) {      // 100, par 100
            for (int nbS = 1; nbS <= 51; nbS+=10) { // 10 par 10
                System.out.print("\n" + l + "; " + nbS + "; ");

                List<RanSegment> allRanSegments = new ArrayList<>();
                for (int g = 0; g < gpxs.length; g++) {
                    // Segments
                    List<Segment> segments = getSegments(gpxs[g], l);
                    Boolean isRace = race[g];
                    List<RanSegment> ranSegments = getRanSegments(segments, nbS, isRace);
                    allRanSegments.addAll(ranSegments);
                }
                calculateRegression(allRanSegments);
            }
        }
    }

    private void calculateRegression(List<RanSegment> ranSegments) {

        //System.out.println("--- Regression ---");

        int j = 0;
        double[][] xpoints1 = new double[ranSegments.size()][4];
        double[] ypoints1 = new double[ranSegments.size()];
        double[][] xpoints2 = new double[ranSegments.size()][4];
        double[] ypoints2 = new double[ranSegments.size()];
        double[][] xpoints3 = new double[ranSegments.size()][6];
        double[] ypoints3 = new double[ranSegments.size()];
        double[][] xpoints4 = new double[ranSegments.size()][2];
        double[] ypoints4 = new double[ranSegments.size()];

        for (RanSegment segment : ranSegments) {
            xpoints1[j][0] = 1;
            xpoints2[j][0] = 1;
            xpoints3[j][0] = 1;
            xpoints4[j][0] = 1;

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
            xpoints1[j][1] = segment.getTotalEasy();
            xpoints1[j][2] = segment.getTotalMedium();
            xpoints1[j][3] = segment.getTotalHard();
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
            xpoints3[j][4] = segment.getTotalMediumD();
            xpoints3[j][5] = segment.getTotalHardD();
            ypoints3[j] =  segment.getTotalTime();

            xpoints4[j][1] = segment.getTotalDistance();
            ypoints4[j] =  segment.getTotalTime();


            j++;
        }

        if (j > 0) {

            String reg1 = null, reg2 = null, reg3 = null;

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression1 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints1, ypoints1);
                if (regression1.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression1.R2() * 100) + "; ");
                reg1 = regression1.beta(0) + " + " + regression1.beta(1) + "easy + "
                        + regression1.beta(2) + "medium + " + regression1.beta(3) + "hard";

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(xpoints2, ypoints2);
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
                if (regression3.R2() * 100 >= 90)
                    System.out.print("***");
                System.out.print(String.format("%3.2f", regression3.R2() * 100) + "; ");

                reg3 = regression3.beta(0) + " + " + regression3.beta(1) + "easy + "
                        + regression3.beta(2) + "medium + " + regression3.beta(3) + "hard" +
                        + regression3.beta(4) + "mediumD + " + regression3.beta(5) + "hardD";

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
            System.out.println();
//            System.out.println(reg1);
//            System.out.println(reg2);
//            System.out.println(reg3);
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

    @Test
    public void predict() {

//        final String[] gpxs = {
//                "C:\\Users\\gille\\Downloads\\pj1.gpx",
//                "C:\\Users\\gille\\Downloads\\pj2.gpx",
//                "C:\\Users\\gille\\Downloads\\pj3.gpx",
//                "C:\\Users\\gille\\Downloads\\pj4.gpx",
//                "C:\\Users\\gille\\Downloads\\pj5.gpx",
//                "C:\\Users\\gille\\Downloads\\pj6.gpx",
//                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
//                "C:\\Users\\gille\\Downloads\\m1.gpx",
//                "C:\\Users\\gille\\Downloads\\m2.gpx",
//                "C:\\Users\\gille\\Downloads\\m3.gpx",
//                "C:\\Users\\gille\\Downloads\\m4.gpx",
//                "C:\\Users\\gille\\Downloads\\m5.gpx",
//                "C:\\Users\\gille\\Downloads\\m6.gpx",
//                "C:\\Users\\gille\\Downloads\\m7.gpx",
//                "C:\\Users\\gille\\Downloads\\cretes.gpx",
//                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
//
//                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
//                "C:\\Users\\gille\\Downloads\\plaine7.gpx",
//
//                "C:\\Users\\gille\\Downloads\\ultra.gpx",
//                "C:\\Users\\gille\\Downloads\\orcier.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
//                "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
//                "C:\\Users\\gille\\Downloads\\long1.gpx",
//                "C:\\Users\\gille\\Downloads\\allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\cfer.gpx",
//                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
//                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",
//                "C:\\Users\\gille\\Downloads\\20220423_MontForchat.gpx",
//                "C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx",
//                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx"      // Ballaison
//
//        };


        final String[] gpxs = {
                "C:\\Users\\gille\\Downloads\\Alice\\Alice1-145.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice2.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice3.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice4.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice5.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice6.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice7.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice8.gpx",
//                "C:\\Users\\gille\\Downloads\\Alice\\Alice9.gpx",
                "C:\\Users\\gille\\Downloads\\West_Highland_Way_Race_2019.gpx"
        };


        for (int l = 100; l <= 100; l += 100) {      // 100, par 100
            for (int nbS = 5; nbS <= 5; nbS+=5) { // 10 par 10
                System.out.print("\n" + l + "; " + nbS + "; ");

                for (int g = 0; g < gpxs.length; g++) {
                    // Segments
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

        // Alice
//        -549.8552316635556 + 0.45326415075985327easy + 1.1307274043675313medium + 0.8983640795888473hard
//                -482.69586870818915 + 0.46254591713329857distance + 6.784978353858557gain + 5.6883784221961loss
//                -654.8269125217936 + 0.5275703697455396easy + 1.3008991094172053medium + -0.5163523739781466hard-0.25870162726008mediumD + 1.245848458339393hardD
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

            // Gilles
//            double predict2 = -357.8396287730519 + (0.3216431232694592 * distance) + (3.22251035216564 * gain)
//                    - (1.8061784068402824 * loss);
//            double predict3 = -135.9541404448948 + (0.2920654632697146 * easy) + (0.5319590280882981 * medium)
//                    + (1.1874294265186975 * hard) + (0.26603038924053984 * mediumD) + (0.7346353959083033 * hardD);

            // Alice
            double predict2 = -482.69586870818915 + (0.46254591713329857 * distance) + (6.784978353858557 * gain)
                    + (5.6883784221961 * loss);
            double predict3 = -654.8269125217936 + (0.5275703697455396 * easy) + (1.3008991094172053 * medium)
                    + -(0.5163523739781466 * hard) - (0.25870162726008 * mediumD) + (1.245848458339393 * hardD);

            predict2 = predict2 > 0 ? predict2 : 0.0;
            predict3 = predict3 > 0 ? predict3 : 0.0;

            System.out.println(distance + "\t" + /*LocalTime.ofSecondOfDay(time)*/ makeReadable(time)  + "\t"
                    //+ LocalTime.ofSecondOfDay((long) predict2) + "\t" + LocalTime.ofSecondOfDay((long) predict3)
                    + "\t" + makeReadable((long) predict2) + "\t" + makeReadable((long) predict3)
            );
        }
        System.out.println(name);
        System.out.println("------------------------------------------------------------------------------------");
    }

    private List<RanSegment> getRanSegments(List<Segment> segments, int nbSegments, boolean isRace) {
        List<RanSegment> ranSegments = new ArrayList<>();


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
                next300 = 0;
                last300 = 0;
            } //else {
                Double rGrade = Grade.GetGradeReference(segment.getGrade()) * 100;
                if (rGrade >= 2 && rGrade < 10) {
                    totalMediumSegment += segment.getDistance();
                    totalTimeMediumSegment += segment.getTime();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade >= 10 && rGrade < 20) {
                    totalHardSegment += segment.getDistance();
                    totalTimeHardSegment += segment.getTime();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade >= 20) {
                    totalVeryHardSegment += segment.getDistance();
                    totalTimeVeryHardSegment += segment.getTime();
                    elevationGainSegment += segment.getElevation();
                } else if (rGrade < -20) {
                    totalVeryHardDSegment += segment.getDistance();
                    totalTimeVeryHardDSegment += segment.getTime();
                    elevationLossSegment += segment.getElevation();
                } else if (rGrade >= -20 && rGrade < -10) {
                    totalHardDSegment += segment.getDistance();
                    totalTimeHardDSegment += segment.getTime();
                    elevationLossSegment += segment.getElevation();
                } else if (rGrade >= -10 && rGrade < -2) {
                    totalMediumDSegment += segment.getDistance();
                    totalTimeMediumDSegment += segment.getTime();
                    elevationLossSegment += segment.getElevation();
                } else {
                    totalEasySegment += segment.getDistance();
                    totalTimeEasySegment += segment.getTime();
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

    public static String makeReadable(long seconds) {

        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));

    }
}
