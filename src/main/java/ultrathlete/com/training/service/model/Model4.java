package ultrathlete.com.training.service.model;

import ultrathlete.com.training.model.RunningData;

import javax.persistence.*;

@Entity
@Table(name = "model4", schema = "ultratraining")
public class Model4 extends AbstractModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long model4Id;
    private double constant;
    private double distance;
    private double r2;

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "Model4{" +
                ", constant=" + constant +
                ", distance=" + distance +
                ", r2=" + r2 +
                '}';
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    @Override
    public double predict(RunningData running) {
        return running == null ? 0.0 :
                constant
                + running.getDistance() * distance;
    }
}
