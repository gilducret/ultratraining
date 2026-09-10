package ultrathlete.com.training.bo;

import io.jenetics.jpx.WayPoint;

/**
 * This class stores all the possible data during running
 * - the data related to the underlying segment
 * - the total from the beginning of the run, till (and including) the related segment
 */
public class RanSegment {

    private Segment segment;
    private Double totalUp = 0.0;
    private Double totalDown = 0.0;
    private Double totalVUp = 0.0;
    private Double totalVDown = 0.0;
    private Double totalFlat = 0.0;
    private Long totalTimeUp = 0L;
    private Long totalTimeDown = 0L;
    private Long totalTimeVUp = 0L;
    private Long totalTimeVDown = 0L;
    private Long totalTimeFlat = 0L;
    private Integer last150 = 0;
    private Integer elevationGain = 0;
    private Integer elevationLoss = 0;
    private Session ranSession;
    private Integer elevationGainMedium;
    private Integer elevationGainHard;
    private Integer elevationGainVeryHard;
    private Integer elevationLossMedium;
    private Integer elevationLossHard;
    private Integer elevationLossVeryHard;

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

    private RanSegment next = null;
    private int next300 = 0;

    private int altitude = 0;
    private double speedBefore = 0.0;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    public Integer getElevationGainMedium() {
        return elevationGainMedium;
    }

    public void setElevationGainMedium(Integer elevationGainMedium) {
        this.elevationGainMedium = elevationGainMedium;
    }

    public Integer getElevationGainHard() {
        return elevationGainHard;
    }

    public void setElevationGainHard(Integer elevationGainHard) {
        this.elevationGainHard = elevationGainHard;
    }

    public Integer getElevationGainVeryHard() {
        return elevationGainVeryHard;
    }

    public void setElevationGainVeryHard(Integer elevationGainVeryHard) {
        this.elevationGainVeryHard = elevationGainVeryHard;
    }

    public Integer getElevationLossMedium() {
        return elevationLossMedium;
    }

    public void setElevationLossMedium(Integer elevationLossMedium) {
        this.elevationLossMedium = elevationLossMedium;
    }

    public Integer getElevationLossHard() {
        return elevationLossHard;
    }

    public void setElevationLossHard(Integer elevationLossHard) {
        this.elevationLossHard = elevationLossHard;
    }

    public Integer getElevationLossVeryHard() {
        return elevationLossVeryHard;
    }

    public void setElevationLossVeryHard(Integer elevationLossVeryHard) {
        this.elevationLossVeryHard = elevationLossVeryHard;
    }

    public double getSpeedBefore() {
        return speedBefore;
    }

    public void setSpeedBefore(double speedBefore) {
        this.speedBefore = speedBefore;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getLast300() {
        return last300;
    }

    public void setLast300(int last300) {
        this.last300 = last300;
    }

    private int last300 = 0;

    public int getNext300() {
        return next300;
    }

    public void setNext300(int next300) {
        this.next300 = next300;
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

    public int getElevationGainSegment() {
        return elevationGainSegment;
    }

    public void setElevationGainSegment(int elevationGainSegment) {
        this.elevationGainSegment = elevationGainSegment;
    }

    public int getElevationLossSegment() {
        return elevationLossSegment;
    }

    public void setElevationLossSegment(int elevationLossSegment) {
        this.elevationLossSegment = elevationLossSegment;
    }

    public double getTotalTimeEasy() {
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

    private RanSegment previous = null;

    public RanSegment getPrevious() {
        return previous;
    }

    public void setPrevious(RanSegment previous) {
        this.previous = previous;
    }

    public RanSegment getNext() {
        return next;
    }

    public void setNext(RanSegment next) {
        this.next = next;
    }

    public Double getTotalVUp() {
        return totalVUp;
    }

    public void setTotalVUp(Double totalVUp) {
        this.totalVUp = totalVUp;
    }

    public Double getTotalVDown() {
        return totalVDown;
    }

    public void setTotalVDown(Double totalVDown) {
        this.totalVDown = totalVDown;
    }

    public Long getTotalTimeVUp() {
        return totalTimeVUp;
    }

    public void setTotalTimeVUp(Long totalTimeVUp) {
        this.totalTimeVUp = totalTimeVUp;
    }

    public Long getTotalTimeVDown() {
        return totalTimeVDown;
    }

    public void setTotalTimeVDown(Long totalTimeVDown) {
        this.totalTimeVDown = totalTimeVDown;
    }

    public RanSegment() {
    }

    public RanSegment(Segment segment) {
        this.segment = segment;
    }

    public Integer getElevationGain() {
        return elevationGain;
    }

    public Session getRanSession() {
        return ranSession;
    }

    public void setRanSession(Session ranSession) {
        this.ranSession = ranSession;
    }

    public void setElevationGain(Integer elevationGain) {
        this.elevationGain = elevationGain;
    }

    public Integer getElevationLoss() {
        return elevationLoss;
    }

    public void setElevationLoss(Integer elevationLoss) {
        this.elevationLoss = elevationLoss;
    }

    public Integer getLast150() {
        return last150;
    }

    public void setLast150(Integer last150) {
        this.last150 = last150;
    }

    public Long getTotalTime() {
        return totalTimeEasy + totalTimeMedium + totalTimeHard + totalTimeVeryHard +
                totalTimeEasyD + totalTimeMediumD + totalTimeHardD + totalTimeVeryHardD;
    }

    public Double getTotalDistance() {
        return totalEasy + totalMedium + totalHard + totalVeryHard +
                totalEasyD + totalMediumD + totalHardD + totalVeryHardD;
        //return totalDown + totalUp + totalFlat;
    }

    public Long getTotalTimeUp() {
        return totalTimeUp;
    }

    public void setTotalTimeUp(Long totalTimeUp) {
        this.totalTimeUp = totalTimeUp;
    }

    public Long getTotalTimeDown() {
        return totalTimeDown;
    }

    public void setTotalTimeDown(Long totalTimeDown) {
        this.totalTimeDown = totalTimeDown;
    }

    public Long getTotalTimeFlat() {
        return totalTimeFlat;
    }

    public void setTotalTimeFlat(Long totalTimeFlat) {
        this.totalTimeFlat = totalTimeFlat;
    }

    public Double getTotalUp() {
        return totalUp;
    }

    public void setTotalUp(Double totalUp) {
        this.totalUp = totalUp;
    }

    public Double getTotalDown() {
        return totalDown;
    }

    public void setTotalDown(Double totalDown) {
        this.totalDown = totalDown;
    }

    public Double getTotalFlat() {
        return totalFlat;
    }

    public void setTotalFlat(Double totalFlat) {
        this.totalFlat = totalFlat;
    }

    public Integer getElevation() {
        return segment != null? segment.getElevation() : 0;
    }

    public Double getSpeed() {
        if (getTime() == 0)
            return 0.0;

        return getDistance() / getTime();
        //return segment != null ? segment.getSpeed() : 0.0;
    }

    public Integer getElevationFrom() {
        return segment != null ? segment.getElevationFrom() : 0;
    }

    public Integer getElevationTo() {
        return segment != null ? segment.getElevationTo() : 0;
    }

    public Double getGrade() {
        return segment != null ? segment.getGrade() : 0.0;
    }

    public WayPoint getFrom() {
        return segment != null ? segment.getFrom() : null;
    }

    public WayPoint getTo() {
        return segment != null ? segment.getTo() : null;
    }

    public Double getDistance() {
        //return segment != null ? segment.getDistance() : null;
        return totalEasySegment + totalMediumSegment + totalHardSegment + totalVeryHardSegment +
                totalEasyDSegment + totalMediumDSegment + totalHardDSegment + totalVeryHardDSegment;
    }

    public Long getTime() {
        return totalTimeEasySegment + totalTimeMediumSegment + totalTimeHardSegment + totalTimeVeryHardSegment +
                totalTimeEasyDSegment + totalTimeMediumDSegment + totalTimeHardDSegment + totalTimeVeryHardDSegment;
        //return segment != null ? segment.getTime() : null;
    }

    public Long getSegmentTime() {
        return segment != null ? segment.getTime() : 0;
    }

    public double getTotalEasySegment() {
        return totalEasySegment;
    }

    public void setTotalEasySegment(double totalEasySegment) {
        this.totalEasySegment = totalEasySegment;
    }

    public double getTotalMediumSegment() {
        return totalMediumSegment;
    }

    public void setTotalMediumSegment(double totalMediumSegment) {
        this.totalMediumSegment = totalMediumSegment;
    }

    public double getTotalHardSegment() {
        return totalHardSegment;
    }

    public void setTotalHardSegment(double totalHardSegment) {
        this.totalHardSegment = totalHardSegment;
    }

    public double getTotalVeryHardSegment() {
        return totalVeryHardSegment;
    }

    public void setTotalVeryHardSegment(double totalVeryHardSegment) {
        this.totalVeryHardSegment = totalVeryHardSegment;
    }

    public double getTotalEasyDSegment() {
        return totalEasyDSegment;
    }

    public void setTotalEasyDSegment(double totalEasyDSegment) {
        this.totalEasyDSegment = totalEasyDSegment;
    }

    public double getTotalMediumDSegment() {
        return totalMediumDSegment;
    }

    public void setTotalMediumDSegment(double totalMediumDSegment) {
        this.totalMediumDSegment = totalMediumDSegment;
    }

    public double getTotalHardDSegment() {
        return totalHardDSegment;
    }

    public void setTotalHardDSegment(double totalHardDSegment) {
        this.totalHardDSegment = totalHardDSegment;
    }

    public double getTotalVeryHardDSegment() {
        return totalVeryHardDSegment;
    }

    public void setTotalVeryHardDSegment(double totalVeryHardDSegment) {
        this.totalVeryHardDSegment = totalVeryHardDSegment;
    }

    public long getTotalTimeEasySegment() {
        return totalTimeEasySegment;
    }

    public void setTotalTimeEasySegment(long totalTimeEasySegment) {
        this.totalTimeEasySegment = totalTimeEasySegment;
    }

    public long getTotalTimeMediumSegment() {
        return totalTimeMediumSegment;
    }

    public void setTotalTimeMediumSegment(long totalTimeMediumSegment) {
        this.totalTimeMediumSegment = totalTimeMediumSegment;
    }

    public long getTotalTimeHardSegment() {
        return totalTimeHardSegment;
    }

    public void setTotalTimeHardSegment(long totalTimeHardSegment) {
        this.totalTimeHardSegment = totalTimeHardSegment;
    }

    public long getTotalTimeVeryHardSegment() {
        return totalTimeVeryHardSegment;
    }

    public void setTotalTimeVeryHardSegment(long totalTimeVeryHardSegment) {
        this.totalTimeVeryHardSegment = totalTimeVeryHardSegment;
    }

    public long getTotalTimeEasyDSegment() {
        return totalTimeEasyDSegment;
    }

    public void setTotalTimeEasyDSegment(long totalTimeEasyDSegment) {
        this.totalTimeEasyDSegment = totalTimeEasyDSegment;
    }

    public long getTotalTimeMediumDSegment() {
        return totalTimeMediumDSegment;
    }

    public void setTotalTimeMediumDSegment(long totalTimeMediumDSegment) {
        this.totalTimeMediumDSegment = totalTimeMediumDSegment;
    }

    public long getTotalTimeHardDSegment() {
        return totalTimeHardDSegment;
    }

    public void setTotalTimeHardDSegment(long totalTimeHardDSegment) {
        this.totalTimeHardDSegment = totalTimeHardDSegment;
    }

    public long getTotalTimeVeryHardDSegment() {
        return totalTimeVeryHardDSegment;
    }

    public void setTotalTimeVeryHardDSegment(long totalTimeVeryHardDSegment) {
        this.totalTimeVeryHardDSegment = totalTimeVeryHardDSegment;
    }

    @Override
    public String toString() {
        return "RanSegment{" +
                "totalUp=" + totalUp +
                ", totalDown=" + totalDown +
                ", totalVUp=" + totalVUp +
                ", totalVDown=" + totalVDown +
                ", totalFlat=" + totalFlat +
                ", totalTimeUp=" + totalTimeUp +
                ", totalTimeDown=" + totalTimeDown +
                ", totalTimeVUp=" + totalTimeVUp +
                ", totalTimeVDown=" + totalTimeVDown +
                ", totalTimeFlat=" + totalTimeFlat +
                ", last150=" + last150 +
                ", elevationGain=" + elevationGain +
                ", elevationLoss=" + elevationLoss +
                ", ranSession=" + ranSession +
                ", totalEasySegment=" + totalEasySegment +
                ", totalMediumSegment=" + totalMediumSegment +
                ", totalHardSegment=" + totalHardSegment +
                ", totalVeryHardSegment=" + totalVeryHardSegment +
                ", totalEasyDSegment=" + totalEasyDSegment +
                ", totalMediumDSegment=" + totalMediumDSegment +
                ", totalHardDSegment=" + totalHardDSegment +
                ", totalVeryHardDSegment=" + totalVeryHardDSegment +
                ", totalTimeEasySegment=" + totalTimeEasySegment +
                ", totalTimeMediumSegment=" + totalTimeMediumSegment +
                ", totalTimeHardSegment=" + totalTimeHardSegment +
                ", totalTimeVeryHardSegment=" + totalTimeVeryHardSegment +
                ", totalTimeEasyDSegment=" + totalTimeEasyDSegment +
                ", totalTimeMediumDSegment=" + totalTimeMediumDSegment +
                ", totalTimeHardDSegment=" + totalTimeHardDSegment +
                ", totalTimeVeryHardDSegment=" + totalTimeVeryHardDSegment +
                ", totalTimeEasy=" + totalTimeEasy +
                ", totalTimeMedium=" + totalTimeMedium +
                ", totalTimeHard=" + totalTimeHard +
                ", totalTimeVeryHard=" + totalTimeVeryHard +
                ", totalTimeEasyD=" + totalTimeEasyD +
                ", totalTimeMediumD=" + totalTimeMediumD +
                ", totalTimeHardD=" + totalTimeHardD +
                ", totalTimeVeryHardD=" + totalTimeVeryHardD +
                ", elevationGainSegment=" + elevationGainSegment +
                ", elevationLossSegment=" + elevationLossSegment +
                '}';
    }
}
