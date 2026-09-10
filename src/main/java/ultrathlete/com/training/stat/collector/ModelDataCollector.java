package ultrathlete.com.training.stat.collector;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.model.Model2Data;
import ultrathlete.com.training.model.Model3Data;
import ultrathlete.com.training.model.Model4Data;

import java.util.List;

public class ModelDataCollector {

    private Model2Data model2Data;
    private Model3Data model3Data;
    private Model4Data model4Data;

    public Model2Data getModel2Data() {
        return model2Data;
    }

    public Model3Data getModel3Data() {
        return model3Data;
    }

    public Model4Data getModel4Data() {
        return model4Data;
    }

    public void collectModel2Data(int j, Model2Data model, RanSegment segment) {
        model2Data.setX(j, 1, segment.getTotalDistance());
        model2Data.setX(j, 2, segment.getElevationGain());
        model2Data.setX(j, 3, segment.getElevationLoss());
        model2Data.setY(j, segment.getTotalTime());
    }

    public void collectModel3Data(int j, Model3Data model, RanSegment segment) {
        model3Data.setX(j, 1, segment.getTotalEasy());
        model3Data.setX(j, 2, segment.getTotalMedium());
        model3Data.setX(j, 3, segment.getTotalHard());
        model3Data.setX(j, 4, segment.getTotalMediumD());
        model3Data.setX(j, 5, segment.getTotalHardD());
        model3Data.setX(j, 6, segment.getTotalVeryHard());
        model3Data.setX(j, 7, segment.getTotalVeryHardD());
        model3Data.setY(j, segment.getTotalTime());
    }

    public void collectModel4Data(int j, Model4Data model, RanSegment segment) {
        model4Data.setX(j,1, segment.getTotalDistance());
        model4Data.setY(j, segment.getTotalTime());
    }

    public void collectModelData(List<RanSegment> ranSegments) {

        if (ranSegments != null && ranSegments.size() > 0) {
            model2Data = new Model2Data(ranSegments.size());
            model3Data = new Model3Data(ranSegments.size());
            model4Data = new Model4Data(ranSegments.size());

            int j = 0;
            for (RanSegment segment : ranSegments) {

                model2Data.setX(j, 0, 1);
                model3Data.setX(j, 0, 1);
                model4Data.setX(j, 0, 1);

                collectModel2Data(j, model2Data, segment);
                collectModel3Data(j, model3Data, segment);
                collectModel4Data(j, model4Data, segment);

                j++;
            }
        }
    }
}
