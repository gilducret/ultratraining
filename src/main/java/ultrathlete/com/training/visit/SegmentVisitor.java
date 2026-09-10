package ultrathlete.com.training.visit;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;

public interface SegmentVisitor {

    void visit(Segment segment);
    void visit(RanSegment segment);
    void update(RanSegment segment);
}
