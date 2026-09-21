package ultrathlete.com.training.model;

public class RaceAnalysisResponse {
    private int sessionId;
    private RaceAnalysis analysis;

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }
    public RaceAnalysis getAnalysis() { return analysis; }
    public void setAnalysis(RaceAnalysis analysis) { this.analysis = analysis; }
}