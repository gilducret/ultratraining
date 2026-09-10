package ultrathlete.com.training.service.model;

import ultrathlete.com.training.model.RunningData;

public abstract class AbstractModel {

    public abstract double predict(RunningData running);
}
