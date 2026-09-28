package ultrathlete.com.training.model;

import java.util.List;

public class MobileSession {
    private int sessionId;
    private String raceName;
    private List<MobileSegment> segments;

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public List<MobileSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<MobileSegment> segments) {
        this.segments = segments;
    }
    // getters + setters
}
