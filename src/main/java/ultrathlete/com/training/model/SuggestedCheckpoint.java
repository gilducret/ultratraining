package ultrathlete.com.training.model;

public class SuggestedCheckpoint {
    private String name;
    private double distanceM;
    private int elevationM;
    private String type; // "waypoint" or "peak"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistanceM() {
        return distanceM;
    }

    public void setDistanceM(double distanceM) {
        this.distanceM = distanceM;
    }

    public int getElevationM() {
        return elevationM;
    }

    public void setElevationM(int elevationM) {
        this.elevationM = elevationM;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
