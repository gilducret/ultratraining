package ultrathlete.com.training.model;

public class SegmentForecast {

    private String name;
    private String distance;
    private int elevationGain;
    private int elevationLoss;
    private String min;
    private String max;
    private double duration2;
    private double duration3;
    private double duration4;

    public double getDuration2() {
        return duration2;
    }

    public void setDuration2(double duration2) {
        this.duration2 = duration2;
    }

    public double getDuration3() {
        return duration3;
    }

    public void setDuration3(double duration3) {
        this.duration3 = duration3;
    }

    public double getDuration4() {
        return duration4;
    }

    public void setDuration4(double duration4) {
        this.duration4 = duration4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(int elevationGain) {
        this.elevationGain = elevationGain;
    }

    public int getElevationLoss() {
        return elevationLoss;
    }

    public void setElevationLoss(int elevationLoss) {
        this.elevationLoss = elevationLoss;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}
