package ultrathlete.com.training.model;

public class RunningData {

    private long time;
    private double distance;
    private long gain;
    private long loss;
    private double easy;
    private double medium;
    private double hard;
    private double mediumD;
    private double hardD;
    private double vHardD;
    private double vHard;

    public RunningData(long time, double distance, long gain, long loss, double easy, double medium, double hard, double mediumD, double hardD, double vHardD, double vHard) {
        this.time = time;
        this.distance = distance;
        this.gain = gain;
        this.loss = loss;
        this.easy = easy;
        this.medium = medium;
        this.hard = hard;
        this.mediumD = mediumD;
        this.hardD = hardD;
        this.vHardD = vHardD;
        this.vHard = vHard;
    }

    public RunningData() {

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getGain() {
        return gain;
    }

    public void setGain(long gain) {
        this.gain = gain;
    }

    public long getLoss() {
        return loss;
    }

    public void setLoss(long loss) {
        this.loss = loss;
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

    public double getvHardD() {
        return vHardD;
    }

    public void setvHardD(double vHardD) {
        this.vHardD = vHardD;
    }

    public double getvHard() {
        return vHard;
    }

    public void setvHard(double vHard) {
        this.vHard = vHard;
    }
}
