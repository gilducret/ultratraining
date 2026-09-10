package ultrathlete.com.training.visit;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;

public class TimeVisitor implements SegmentVisitor {

    private Long totalTimeUp;
    private Long totalTimeDown;
    private Long totalTimeFlat;

    @Override
    public void visit(Segment segment) {
        if (segment.getElevation() > 0) {
            totalTimeUp += segment.getTime();
        } else if (segment.getElevation() < 0) {
            totalTimeDown += segment.getTime();
        } else {
            totalTimeFlat += segment.getTime();
        }
    }

    @Override
    public void visit(RanSegment segment) {

    }

    @Override
    public void update(RanSegment ran) {
        ran.setTotalTimeUp(totalTimeUp);
        ran.setTotalTimeDown(totalTimeDown);
        ran.setTotalTimeFlat(totalTimeFlat);
    }
}
