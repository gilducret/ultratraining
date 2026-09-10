package ultrathlete.com.training.service.model;

import ultrathlete.com.training.model.RunningData;

import jakarta.persistence.*;

@Entity
@Table(name = "model3", schema = "ultratraining")
public class Model3 extends AbstractModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long model3Id;
    private double constant;
    private double easy;
    private double medium;
    private double hard;
    private double mediumD;
    private double hardD;
    private double veryHard;
    private double veryHardD;
    private double r2;

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }

    public double getEasy() {
        return easy;
    }

    public void setEasy(double easy) {
        this.easy = easy;
    }

    public double getMedium() {
        return medium;
    }

    public void setMedium(double medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return "Model3{" +
                ", constant=" + constant +
                ", easy=" + easy +
                ", medium=" + medium +
                ", hard=" + hard +
                ", mediumD=" + mediumD +
                ", hardD=" + hardD +
                ", veryHard=" + veryHard +
                ", veryHardD=" + veryHardD +
                ", r2=" + r2 +
                '}';
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public double getHard() {
        return hard;
    }

    public void setHard(double hard) {
        this.hard = hard;
    }

    public double getMediumD() {
        return mediumD;
    }

    public void setMediumD(double mediumD) {
        this.mediumD = mediumD;
    }

    public double getHardD() {
        return hardD;
    }

    public void setHardD(double hardD) {
        this.hardD = hardD;
    }

    public double getVeryHard() {
        return veryHard;
    }

    public void setVeryHard(double veryHard) {
        this.veryHard = veryHard;
    }

    public double getVeryHardD() {
        return veryHardD;
    }

    public void setVeryHardD(double veryHardD) {
        this.veryHardD = veryHardD;
    }

    @Override
    public double predict(RunningData running) {
        return running == null ? 0.0 :
                constant
                + running.getEasy() * easy
                + running.getMedium() * medium
                + running.getHard() * hard
                + running.getvHard() * veryHard
                + running.getMediumD() * mediumD
                + running.getHardD() * hardD
                + running.getvHardD() * veryHardD;
    }
}
