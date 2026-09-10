package ultrathlete.com.training.bo;

import io.jenetics.jpx.WayPoint;
import ultrathlete.com.training.visit.SegmentVisitor;

import java.util.List;

/**
 * Stores data between 2 waypoints
 */
public class Segment {

    private WayPoint from;
    private WayPoint to;
    private Double distance;
    private Long time;
    private Integer elevationFrom;
    private Integer elevationTo;
    private List<WayPoint> wayPoints;

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public void accept(SegmentVisitor visitor) {
        if (visitor != null) {
            visitor.visit(this);
        }
    }

    public Integer getElevation() {
        return elevationTo - elevationFrom;
    }

    public Double getSpeed() {
        return distance / time;
    }

    public Integer getElevationFrom() {
        return elevationFrom;
    }

    public void setElevationFrom(Integer elevationFrom) {
        this.elevationFrom = elevationFrom;
    }

    public Integer getElevationTo() {
        return elevationTo;
    }

    public void setElevationTo(Integer elevationTo) {
        this.elevationTo = elevationTo;
    }

    public Double getGrade() {
        return getElevation() / distance;
    }

    public WayPoint getFrom() {
        return from;
    }

    public void setFrom(WayPoint from) {
        this.from = from;
    }

    public WayPoint getTo() {
        return to;
    }

    public void setTo(WayPoint to) {
        this.to = to;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
