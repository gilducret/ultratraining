package ultrathlete.com.training.bo.impl;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Session;

import java.util.ArrayList;
import java.util.List;

public class RanSession implements Session {

    private List<RanSegment> ranSegments;

    private Double totalDistanceUp;
    private Double totalDistanceDown;
    private Double totalDistanceFlat;
    private Long totalTimeUp;
    private Long totalTimeDown;
    private Long totalTimeFlat;
    private Integer totalElevationGain;
    private Integer totalElevationLoss;
    private Boolean isRace;

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


    double totalEasy = 0.0;
    double totalMedium = 0.0;
    double totalHard = 0.0;
    double totalVeryHard = 0.0;

    double totalEasyD = 0.0;
    double totalMediumD = 0.0;
    double totalHardD = 0.0;
    double totalVeryHardD = 0.0;

    public Boolean getRace() {
        return isRace;
    }

    public void setRace(Boolean race) {
        isRace = race;
    }

    public Integer getTotalElevationGain() {
        return totalElevationGain;
    }

    public Integer getTotalElevationLoss() {
        return totalElevationLoss;
    }

    public long getTotalTimeEasy() {
        return totalTimeEasy;
    }

    public void setTotalTimeEasy(long totalTimeEasy) {
        this.totalTimeEasy = totalTimeEasy;
    }

    public long getTotalTimeMedium() {
        return totalTimeMedium;
    }

    public void setTotalTimeMedium(long totalTimeMedium) {
        this.totalTimeMedium = totalTimeMedium;
    }

    public long getTotalTimeHard() {
        return totalTimeHard;
    }

    public void setTotalTimeHard(long totalTimeHard) {
        this.totalTimeHard = totalTimeHard;
    }

    public long getTotalTimeVeryHard() {
        return totalTimeVeryHard;
    }

    public void setTotalTimeVeryHard(long totalTimeVeryHard) {
        this.totalTimeVeryHard = totalTimeVeryHard;
    }

    public long getTotalTimeEasyD() {
        return totalTimeEasyD;
    }

    public void setTotalTimeEasyD(long totalTimeEasyD) {
        this.totalTimeEasyD = totalTimeEasyD;
    }

    public long getTotalTimeMediumD() {
        return totalTimeMediumD;
    }

    public void setTotalTimeMediumD(long totalTimeMediumD) {
        this.totalTimeMediumD = totalTimeMediumD;
    }

    public long getTotalTimeHardD() {
        return totalTimeHardD;
    }

    public void setTotalTimeHardD(long totalTimeHardD) {
        this.totalTimeHardD = totalTimeHardD;
    }

    public long getTotalTimeVeryHardD() {
        return totalTimeVeryHardD;
    }

    public void setTotalTimeVeryHardD(long totalTimeVeryHardD) {
        this.totalTimeVeryHardD = totalTimeVeryHardD;
    }

    public void setElevationGain(int elevationGain) {
        this.elevationGain = elevationGain;
    }

    public void setElevationLoss(int elevationLoss) {
        this.elevationLoss = elevationLoss;
    }

    public double getTotalEasy() {
        return totalEasy;
    }

    public void setTotalEasy(double totalEasy) {
        this.totalEasy = totalEasy;
    }

    public double getTotalMedium() {
        return totalMedium;
    }

    public void setTotalMedium(double totalMedium) {
        this.totalMedium = totalMedium;
    }

    public double getTotalHard() {
        return totalHard;
    }

    public void setTotalHard(double totalHard) {
        this.totalHard = totalHard;
    }

    public double getTotalVeryHard() {
        return totalVeryHard;
    }

    public void setTotalVeryHard(double totalVeryHard) {
        this.totalVeryHard = totalVeryHard;
    }

    public double getTotalEasyD() {
        return totalEasyD;
    }

    public void setTotalEasyD(double totalEasyD) {
        this.totalEasyD = totalEasyD;
    }

    public double getTotalMediumD() {
        return totalMediumD;
    }

    public void setTotalMediumD(double totalMediumD) {
        this.totalMediumD = totalMediumD;
    }

    public double getTotalHardD() {
        return totalHardD;
    }

    public void setTotalHardD(double totalHardD) {
        this.totalHardD = totalHardD;
    }

    public double getTotalVeryHardD() {
        return totalVeryHardD;
    }

    public void setTotalVeryHardD(double totalVeryHardD) {
        this.totalVeryHardD = totalVeryHardD;
    }

    @Override
    public List<RanSegment> getRanSegments() {
        return ranSegments;
    }

    @Override
    public Double getTotalDistanceUp() {
        return totalDistanceUp;
    }

    @Override
    public Double getTotalDistanceDown() {
        return totalDistanceDown;
    }

    @Override
    public Double getTotalDistanceFlat() {
        return totalDistanceFlat;
    }

    @Override
    public Double getTotalDistance() {
        return totalDistanceDown + totalDistanceFlat + totalDistanceUp;
    }

    @Override
    public Long getTotalTime() {
        return totalTimeDown + totalTimeUp + totalTimeFlat;
    }

    @Override
    public Long getTotalTimeUp() {
        return totalTimeUp;
    }

    @Override
    public Long getTotalTimeDown() {
        return totalTimeDown;
    }

    @Override
    public Long getTotalTimeFlat() {
        return totalTimeFlat;
    }

    @Override
    public Integer getElevationGain() {
        return elevationGain;
    }

    @Override
    public Integer getElevationLoss() {
        return totalElevationLoss;
    }

    public void setRanSegments(List<RanSegment> ranSegments) {
        this.ranSegments = ranSegments;
    }

    public void setTotalDistanceUp(Double totalDistanceUp) {
        this.totalDistanceUp = totalDistanceUp;
    }

    public void setTotalDistanceDown(Double totalDistanceDown) {
        this.totalDistanceDown = totalDistanceDown;
    }

    public void setTotalDistanceFlat(Double totalDistanceFlat) {
        this.totalDistanceFlat = totalDistanceFlat;
    }

    public void setTotalTimeUp(Long totalTimeUp) {
        this.totalTimeUp = totalTimeUp;
    }

    public void setTotalTimeDown(Long totalTimeDown) {
        this.totalTimeDown = totalTimeDown;
    }

    public void setTotalTimeFlat(Long totalTimeFlat) {
        this.totalTimeFlat = totalTimeFlat;
    }


    public void setTotalElevationGain(Integer totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }

    public void setTotalElevationLoss(Integer totalElevationLoss) {
        this.totalElevationLoss = totalElevationLoss;
    }
}
