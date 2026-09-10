package ultrathlete.com.training.stat.collector;

import ultrathlete.com.training.bo.Session;
import ultrathlete.com.training.visit.SegmentVisitor;

import java.util.List;

public interface StatCollector {
    Session collect(String gpxFile, List<SegmentVisitor> visitors);
}
