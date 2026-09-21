package ultrathlete.com.training.model;

import java.util.List;

public class MobileSegment {
    private String name;
    private double distanceFromStartM;
    private long runningTimeModel2;  // seconds for THIS section only
    private long runningTimeModel3;  // seconds for THIS section only
    private List<MobileGpsPoint> gpsPoints;

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

    public long getRunningTimeModel2() {
        return runningTimeModel2;
    }

    public void setRunningTimeModel2(long runningTimeModel2) {
        this.runningTimeModel2 = runningTimeModel2;
    }

    public long getRunningTimeModel3() {
        return runningTimeModel3;
    }

    public void setRunningTimeModel3(long runningTimeModel3) {
        this.runningTimeModel3 = runningTimeModel3;
    }

    public List<MobileGpsPoint> getGpsPoints() {
        return gpsPoints;
    }

    public void setGpsPoints(List<MobileGpsPoint> gpsPoints) {
        this.gpsPoints = gpsPoints;
    }
}
