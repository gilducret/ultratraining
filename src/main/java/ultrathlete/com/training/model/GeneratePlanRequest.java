package ultrathlete.com.training.model;

import java.util.List;

public class GeneratePlanRequest {
    private int sessionId;
    private String raceName;

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

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

    private List<Checkpoint> checkpoints;

    public static class Checkpoint {
        private String name;
        private double distanceKm;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getDistanceKm() {
            return distanceKm;
        }

        public void setDistanceKm(double distanceKm) {
            this.distanceKm = distanceKm;
        }
        // getters + setters
    }
    // getters + setters
}

