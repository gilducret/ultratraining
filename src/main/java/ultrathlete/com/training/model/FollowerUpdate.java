package ultrathlete.com.training.model;

import java.util.Date;
import java.util.List;

public class FollowerUpdate {
    private boolean raceStarted;
    private double latitude;
    private double longitude;
    private double distanceFromStartM;
    private int    elapsedSeconds;
    private Date lastUpdate;
    private List<FollowerCheckpoint> checkpointEtas;

    public boolean isRaceStarted() {
        return raceStarted;
    }

    public void setRaceStarted(boolean raceStarted) {
        this.raceStarted = raceStarted;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDistanceFromStartM() {
        return distanceFromStartM;
    }

    public void setDistanceFromStartM(double distanceFromStartM) {
        this.distanceFromStartM = distanceFromStartM;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<FollowerCheckpoint> getCheckpointEtas() {
        return checkpointEtas;
    }

    public void setCheckpointEtas(List<FollowerCheckpoint> checkpointEtas) {
        this.checkpointEtas = checkpointEtas;
    }
}
