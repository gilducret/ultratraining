package ultrathlete.com.training.model;

import java.util.Date;

public class SessionSummary {
    private int id;
    private String name;
    private Date date;
    private String status;
    private double totalDistanceM;
    private int segmentCount;    // number of checkpoints

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalDistanceM() { return totalDistanceM; }
    public void setTotalDistanceM(double totalDistanceM) {
        this.totalDistanceM = totalDistanceM; }
    public int getSegmentCount() { return segmentCount; }
    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount; }
}
