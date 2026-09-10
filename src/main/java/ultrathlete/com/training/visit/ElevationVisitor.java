package ultrathlete.com.training.visit;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;

public class ElevationVisitor implements SegmentVisitor {

    private Integer elevationGain;
    private Integer elevationLoss;

    @Override
    public void visit(Segment segment) {
        Integer elevation = segment.getElevation();
        if (elevation > 0) {
            elevationGain += segment.getElevation();
        } else if (elevation < 0) {
            elevationLoss += segment.getElevation();
        }
    }

    @Override
    public void visit(RanSegment segment) {

    }

    @Override
    public void update(RanSegment segment) {

    }
}
