package ultrathlete.com.training.service.model;

import ultrathlete.com.training.model.RunningData;

import javax.persistence.*;

@Entity
@Table(name = "model2", schema = "ultratraining")
public class Model2 extends AbstractModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long model2Id;

    @Override
    public String toString() {
        return "Model2{" +
                ", constant=" + constant +
                ", distance=" + distance +
                ", gain=" + gain +
                ", loss=" + loss +
                ", r2=" + r2 +
                '}';
    }

    private double constant;
    private double distance;
    private double gain;
    private double loss;
    private double r2;

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double predict(RunningData running) {
        return running == null ? 0.0 :
                constant
                + running.getDistance() * distance
                + running.getGain() * gain
                + running.getLoss() * loss;
    }
}
