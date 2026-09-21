package ultrathlete.com.training.model;

import java.util.List;

public class FollowerSessionInfo {
    private int sessionId;
    private String raceName;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public List<double[]> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<double[]> routePoints) {
        this.routePoints = routePoints;
    }

    public List<FollowerCheckpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<FollowerCheckpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    private List<double[]> routePoints;
    private List<FollowerCheckpoint> checkpoints;
    private List<double[]> elevationProfile; // [distanceM, elevationM]
    private double totalDistanceM;

    // add getters + setters for the two new fields
    public List<double[]> getElevationProfile() { return elevationProfile; }
    public void setElevationProfile(List<double[]> e) { this.elevationProfile = e; }
    public double getTotalDistanceM() { return totalDistanceM; }
    public void setTotalDistanceM(double d) { this.totalDistanceM = d; }
}