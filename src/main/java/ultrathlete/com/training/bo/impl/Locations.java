package ultrathlete.com.training.bo.impl;

import java.util.ArrayList;
import java.util.List;

public class Locations {

    private List<LocationImpl> locations = new ArrayList<>();

    public void addLocation(LocationImpl location) {
        locations.add(location);
    }

    public List<LocationImpl> getLocations() {
        return locations;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ locations: [");
        for (LocationImpl location : locations) {
            buffer.append(location.toString());
        }
        buffer.append("]}");
        return buffer.toString();
    }
}
