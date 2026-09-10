package ultrathlete.com.training.bo.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ultrathlete.com.training.bo.Location;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationImpl implements Location {

    private double latitude;
    private double longitude;
    private double elevation;

    public LocationImpl(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public LocationImpl(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = 0.0;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getElevation() {
        return elevation;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ \"latitude\": ");
        buffer.append(latitude);
        buffer.append(", \"longitude\" : ");
        buffer.append(longitude);
        buffer.append(", \"elevation\" : ");
        buffer.append(elevation);
        buffer.append("}");
        return buffer.toString();
    }
}
