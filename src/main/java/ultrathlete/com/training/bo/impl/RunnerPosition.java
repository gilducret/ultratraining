package ultrathlete.com.training.bo.impl;

import jakarta.persistence.*;
import java.util.Date;
@Entity
@Table(name = "runnerposition", schema = "ultratraining")
public class RunnerPosition {

    @Id
    @Column(name = "sessionid")
    private int sessionId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "altitude")
    private double altitude;

    @Column(name = "distancefromstartm")
    private double distanceFromStartM;

    @Column(name = "elapsedseconds")
    private int elapsedSeconds;

    @Column(name = "lastupdate")
    private Date lastUpdate;

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
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

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
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
}