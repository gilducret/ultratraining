package ultrathlete.com.training.service;

import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.Prediction;
import ultrathlete.com.training.bo.impl.UserImpl;

import java.util.Collection;
import java.util.List;

public interface PredictionService {

    PredictedSession addPredictedSession(UserImpl user, PredictedSession session);
    Collection<Prediction> getPredictions(PredictedSession session);
    void removePredictedSession(Integer id);
    void addPredictions(List<Double> distances, PredictedSession session);
    void removePrediction(Integer id);
    PredictedSession getPredictedSession(Integer id);
}
