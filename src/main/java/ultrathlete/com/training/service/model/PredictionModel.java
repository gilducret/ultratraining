package ultrathlete.com.training.service.model;

import ultrathlete.com.training.bo.RanSegment;

/**
 * This describes describes when, for each model, one is supposed to reach the end of a segment
 */
public class PredictionModel {
    private RanSegment ranSegment;
    private double model2;
    private double model3;
    private double model4;

    public PredictionModel(RanSegment ranSegment, double model2, double model3, double model4) {
        this.ranSegment = ranSegment;
        this.model2 = model2;
        this.model3 = model3;
        this.model4 = model4;
    }

    public RanSegment getRanSegment() {
        return ranSegment;
    }

    public void setRanSegment(RanSegment ranSegment) {
        this.ranSegment = ranSegment;
    }

    public double getModel2() {
        return model2;
    }

    public void setModel2(double model2) {
        this.model2 = model2;
    }

    public double getModel3() {
        return model3;
    }

    public void setModel3(double model3) {
        this.model3 = model3;
    }

    public double getModel4() {
        return model4;
    }

    public void setModel4(double model4) {
        this.model4 = model4;
    }
}
