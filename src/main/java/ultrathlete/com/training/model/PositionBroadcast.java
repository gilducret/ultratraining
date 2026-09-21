package ultrathlete.com.training.model;

public class PositionBroadcast {
    private double latitude;
    private double longitude;
    private double altitude;
    private double distanceFromStartM;
    private int    elapsedSeconds;

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
}
