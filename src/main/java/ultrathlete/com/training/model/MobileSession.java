package ultrathlete.com.training.model;

import java.util.List;

public class MobileSession {
    private String raceName;
    private List<MobileSegment> segments;

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public List<MobileSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<MobileSegment> segments) {
        this.segments = segments;
    }
    // getters + setters
}
