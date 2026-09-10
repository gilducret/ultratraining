package ultrathlete.com.training.service.impl;

import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.service.model.Model2;
import ultrathlete.com.training.service.model.Model3;
import ultrathlete.com.training.service.model.Model4;

public class Predictor {

    public double getModel2Prediction(RanSegment ran, Model2 model2) {
        if (ran == null || model2 == null)
            return 0.0;

        double distance = ran.getTotalDistance();
        long gain = ran.getElevationGain();
        long loss = ran.getElevationLoss();
        return model2.getConstant()
                + model2.getDistance() * distance
                + model2.getGain() * gain
                + model2.getLoss() * loss;
    }

    public double getModel3Prediction(RanSegment ran, Model3 model3) {
        if (ran == null || model3 == null)
            return 0.0;

        double easy = ran.getTotalEasy();
        double medium = ran.getTotalMedium();
        double hard = ran.getTotalHard();
        double mediumD = ran.getTotalMediumD();
        double hardD = ran.getTotalHardD();
        double vHardD = ran.getTotalVeryHardD();
        double vHard = ran.getTotalVeryHard();
        return model3.getConstant()
                + model3.getEasy() * easy
                + model3.getMedium() * medium
                + model3.getHard() * hard
                + model3.getMediumD() * mediumD
                + model3.getHardD() * hardD
                + model3.getVeryHard() * vHard
                + model3.getVeryHardD() * vHardD;
    }

    public double getModel4Prediction(RanSegment ran, Model4 model4) {
        if (ran == null || model4 == null)
            return 0.0;

        double distance = ran.getTotalDistance();
        return model4.getConstant()
                + model4.getDistance() * distance;
    }
}
