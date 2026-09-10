package ultrathlete.com.training.visit;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;

public class DistanceVisitor implements SegmentVisitor {

    private Double totalDistanceUp;
    private Double totalDistanceDown;
    private Double totalDistanceFlat;

    public Double getTotalDistanceUp() {
        return totalDistanceUp;
    }

    public Double getTotalDistanceDown() {
        return totalDistanceDown;
    }

    public Double getTotalDistanceFlat() {
        return totalDistanceFlat;
    }

    public void visit(Segment segment) {
        if (segment.getElevation() > 0) {
            totalDistanceUp += segment.getDistance();
        } else if (segment.getElevation() < 0) {
            totalDistanceDown += segment.getDistance();
        } else {
            totalDistanceFlat += segment.getDistance();
        }
    }
    public void visit(RanSegment segment) {

    }

    public void update(RanSegment ran) {
        ran.setTotalUp(totalDistanceUp);
        ran.setTotalDown(totalDistanceDown);
        ran.setTotalFlat(totalDistanceFlat);
    }
}
