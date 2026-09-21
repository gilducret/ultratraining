package ultrathlete.com.training.model;

public class FollowerCheckpoint {
    private String name;
    private double distanceFromStartM;
    private boolean passed;
    private Long etaTimestamp;  // unix ms, null if passed

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistanceFromStartM() {
        return distanceFromStartM;
    }

    public void setDistanceFromStartM(double distanceFromStartM) {
        this.distanceFromStartM = distanceFromStartM;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Long getEtaTimestamp() {
        return etaTimestamp;
    }

    public void setEtaTimestamp(Long etaTimestamp) {
        this.etaTimestamp = etaTimestamp;
    }
}
