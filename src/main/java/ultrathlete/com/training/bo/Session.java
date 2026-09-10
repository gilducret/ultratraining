package ultrathlete.com.training.bo;

import java.util.List;

public interface Session {

    List<RanSegment> getRanSegments();
    Double getTotalDistanceUp();
    Double getTotalDistanceDown();
    Double getTotalDistanceFlat();
    Double getTotalDistance();
    Long getTotalTime();
    Long getTotalTimeUp();
    Long getTotalTimeDown();
    Long getTotalTimeFlat();
    Integer getElevationGain();
    Integer getElevationLoss();
}
