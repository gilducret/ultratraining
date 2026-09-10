package ultrathlete.com.training.bo.impl;

import java.util.ArrayList;
import java.util.List;

public class ResultLocations {

    private List<LocationImpl> results = new ArrayList<>();

    public void addLocation(LocationImpl location) {
        results.add(location);
    }

    public List<LocationImpl> getLocations() {
        return results;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ results: [");
        for (LocationImpl location : results) {
            buffer.append(location.toString());
        }
        buffer.append("] }");
        return buffer.toString();
    }
}
