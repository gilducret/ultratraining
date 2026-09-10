package ultrathlete.com.training.service.impl;

import ultrathlete.com.data.stats.regression.Grade;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.impl.RanSession;
import ultrathlete.com.training.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class RanSegmentBuilder {

    private List<RanSegment> ranSegments = new ArrayList<>();

    private double totalEasySegment = 0.0;
    private double totalMediumSegment = 0.0;
    private double totalHardSegment = 0.0;
    private double totalVeryHardSegment = 0.0;

    private double totalEasyDSegment = 0.0;
    private double totalMediumDSegment = 0.0;
    private double totalHardDSegment = 0.0;
    private double totalVeryHardDSegment = 0.0;

    private long totalTimeEasySegment = 0L;
    private long totalTimeMediumSegment = 0L;
    private long totalTimeHardSegment = 0L;
    private long totalTimeVeryHardSegment = 0L;

    private long totalTimeEasyDSegment = 0L;
    private long totalTimeMediumDSegment = 0L;
    private  long totalTimeHardDSegment = 0L;
    private long totalTimeVeryHardDSegment = 0L;

    private long totalTimeEasy = 0L;
    private long totalTimeMedium = 0L;
    private long totalTimeHard = 0L;
    private long totalTimeVeryHard = 0L;

    private long totalTimeEasyD = 0L;
    private long totalTimeMediumD = 0L;
    private long totalTimeHardD = 0L;
    private long totalTimeVeryHardD = 0L;

    private int elevationGain = 0;
    private int elevationLoss = 0;
    private int elevationGainSegment = 0;
    private int elevationLossSegment = 0;

    private double totalEasy = 0.0;
    private double totalMedium = 0.0;
    private double totalHard = 0.0;
    private double totalVeryHard = 0.0;

    private double totalEasyD = 0.0;
    private double totalMediumD = 0.0;
    private double totalHardD = 0.0;
    private double totalVeryHardD = 0.0;

    private int next300 = 0;
    private int last300 = 0;

    private Integer elevationGainMedium = 0;
    private Integer elevationGainHard = 0;
    private Integer elevationGainVeryHard = 0;
    private Integer elevationLossMedium = 0;
    private Integer elevationLossHard = 0;
    private Integer elevationLossVeryHard = 0;

    private Integer elevationGainMediumSegment = 0;
    private  Integer elevationGainHardSegment = 0;
    private Integer elevationGainVeryHardSegment = 0;
    private Integer elevationLossMediumSegment = 0;
    private Integer elevationLossHardSegment = 0;
    private Integer elevationLossVeryHardSegment = 0;

    public List<RanSegment> getRanSegments(List<Segment> segments, int nbSegments, boolean isRace) {
        int i = 0;
        RanSegment previous = null;
        RanSession session = new RanSession();
        session.setRace(isRace);

        for (Segment segment : segments) {

            if ((i > 0 && (i % nbSegments == 0))) {
                RanSegment ranSegment = new RanSegment();
                ranSegments.add(ranSegment);
                linkSegments(previous, ranSegment);
                previous = ranSegment;
                // Set the calculated variables
                cumulateSegmentData();
                setSegmentData(ranSegment, segment, session);
                //System.out.println("Ran: " + ranSegment.getTotalDistance() + " - " + TimeUtil.MakeReadable(ranSegment.getTotalTime()));
                resetSegment();
            }
            collectData(segment);
            i++;                    // Goto next segment
        }
        // Did we miss some sections?
        if (segments.size() % nbSegments != 0) {
            RanSegment ranSegment = new RanSegment();
            ranSegments.add(ranSegment);
            //linkSegments(previous, ranSegment);
            cumulateSegmentData();
            setSegmentData(ranSegment, null, session);
            //System.out.println("Last Ran: " + ranSegment.getTotalDistance() + " - " + TimeUtil.MakeReadable(ranSegment.getTotalTime()));
        }
        setSessionData(session);
        session.setRanSegments(ranSegments);
        return ranSegments;
    }

    private void linkSegments(RanSegment previous, RanSegment ranSegment) {
        // Link the segments
        if (previous != null) {
            previous.setNext(ranSegment);
        }
        ranSegment.setPrevious(previous);
    }

    private void resetSegment() {
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
    }

    private void cumulateSegmentData() {
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
    }

    private void setSegmentData(RanSegment ranSegment, Segment segment, RanSession session) {
        ranSegment.setSegment(segment);
        ranSegment.setElevationGainHard(elevationGainHard);
        ranSegment.setElevationGainVeryHard(elevationGainVeryHard);
        ranSegment.setElevationGainMedium(elevationGainMedium);
        ranSegment.setElevationLossHard(elevationLossHard);
        ranSegment.setElevationLossVeryHard(elevationLossVeryHard);
        ranSegment.setElevationLossMedium(elevationLossMedium);
        if (segment != null) {
            ranSegment.setAltitude(segment.getElevationFrom());
        }
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
    }

    private void setSessionData(RanSession session) {
        session.setTotalEasy(totalEasy);
        session.setTotalMedium(totalMedium);
        session.setTotalHard(totalHard);
        session.setTotalVeryHard(totalVeryHard);
        session.setTotalEasyD(totalEasyD);
        session.setTotalMediumD(totalMediumD);
        session.setTotalHardD(totalHardD);
        session.setTotalVeryHardD(totalVeryHardD);
        session.setElevationGain(elevationGain);
        session.setElevationLoss(elevationLoss);
    }

    private void collectData(Segment segment) {
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
    }
}
