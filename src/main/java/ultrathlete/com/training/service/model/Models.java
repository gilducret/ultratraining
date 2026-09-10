package ultrathlete.com.training.service.model;

public class Models {

    private Model2 model2;
    private Model3 model3;
    private Model4 model4;

    public Model4 getModel4() {
        return model4;
    }

    public void setModel4(Model4 model4) {
        this.model4 = model4;
    }

    public Model2 getModel2() {
        return model2;
    }

    public void setModel2(Model2 model2) {
        this.model2 = model2;
    }

    public Model3 getModel3() {
        return model3;
    }

    public void setModel3(Model3 model3) {
        this.model3 = model3;
    }

    @Override
    public String toString() {
        return "Models{" +
                "model2=" + model2 +
                ", model3=" + model3 +
                ", model4=" + model4 +
                '}';
    }
}
