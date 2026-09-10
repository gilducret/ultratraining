package ultrathlete.com.training.model;

import java.util.ArrayList;
import java.util.List;

public class Forecast {

    private String name;
    private double distance;
    private int elevationGain;
    private int elevationLoss;

    private List<SegmentForecast> segments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void add(SegmentForecast segment) {
        segments.add(segment);
    }

    public List<SegmentForecast> getSegments() {
        return segments;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
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
}
