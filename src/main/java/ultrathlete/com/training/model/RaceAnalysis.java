package ultrathlete.com.training.model;

import java.util.List;

// model/RaceAnalysis.java
public class RaceAnalysis {
    private double totalDistanceM;
    private int totalElevationGain;
    private int totalElevationLoss;

    public List<ElevationPoint> getElevationProfile() {
        return elevationProfile;
    }

    public void setElevationProfile(List<ElevationPoint> elevationProfile) {
        this.elevationProfile = elevationProfile;
    }

    public List<SuggestedCheckpoint> getSuggestedCheckpoints() {
        return suggestedCheckpoints;
    }

    public void setSuggestedCheckpoints(List<SuggestedCheckpoint> suggestedCheckpoints) {
        this.suggestedCheckpoints = suggestedCheckpoints;
    }

    private List<ElevationPoint> elevationProfile;
    private List<SuggestedCheckpoint> suggestedCheckpoints;
    // getters + setters

    public double getTotalDistanceM() {
        return totalDistanceM;
    }

    public void setTotalDistanceM(double totalDistanceM) {
        this.totalDistanceM = totalDistanceM;
    }

    public int getTotalElevationLoss() {
        return totalElevationLoss;
    }

    public void setTotalElevationLoss(int totalElevationLoss) {
        this.totalElevationLoss = totalElevationLoss;
    }

    public int getTotalElevationGain() {
        return totalElevationGain;
    }

    public void setTotalElevationGain(int totalElevationGain) {
        this.totalElevationGain = totalElevationGain;
    }
}
