package ultrathlete.com.training.model;

import ultrathlete.com.training.stat.collector.ModelDataCollector;

public class ModelData {

    protected double[][] xpoints;
    protected double[] ypoints;

    public ModelData(int nbElements, int nbVariables) {
        xpoints = new double[nbElements][nbVariables];
        ypoints = new double[nbElements];
    }

    public double[][] getXs() {
        return xpoints;
    }

    public double[] getYs() {
        return ypoints;
    }

    public double getX(int element, int var) {
        return xpoints[element][var];
    }

    public double getY(int element) {
        return ypoints[element];
    }

    public void setX(int element, int var, double value) {
        xpoints[element][var] = value;
    }

    public void setY(int element, double value) {
        ypoints[element] = value;
    }

    public void collectData(ModelDataCollector collector) {
        // Default behaviour
    }
}
