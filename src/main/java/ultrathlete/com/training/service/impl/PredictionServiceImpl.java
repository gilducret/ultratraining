package ultrathlete.com.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.Prediction;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.data.PredictedSessionDAO;
import ultrathlete.com.training.data.PredictionDAO;
import ultrathlete.com.training.service.PredictionService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PredictionServiceImpl implements PredictionService {

    @Autowired
    private PredictedSessionDAO predictedSessionDAO;
    @Autowired
    private PredictionDAO predictionDAO;

    @Override
    public PredictedSession addPredictedSession(UserImpl user, PredictedSession session) {
        if (user == null || session == null) {
            return null;
        }
        session.setUser(user);
        return predictedSessionDAO.save(session);

        /* Ici il faut:
            - appeler ForecastManager pour obtenir les predictions pour le fichier en question
            - prendre la liste des points de prediction
            - prendre tous les ways points entre les points de prediction
            - stocker la structure qui en resulte

            !!! Bien revoir comment on évalue au milieu, entre deux points de prédiction
            -> Il faut tous les running segments de manière à calculer ce qui reste
        */
    }

    @Override
    public Collection<Prediction> getPredictions(PredictedSession session) {
        if (session == null) {
            return predictionDAO.loadPredictions(session.getId());
        } else {
            return null;
        }
    }

    @Override
    public void removePredictedSession(Integer id) {
        predictedSessionDAO.deleteById(id);
    }

    @Override
    public void addPredictions(List<Double> distances, PredictedSession session) {

    }

    @Override
    public void removePrediction(Integer id) {
        predictionDAO.deleteById(id);
    }

    @Override
    public PredictedSession getPredictedSession(Integer id) {
        Optional<PredictedSession> session = predictedSessionDAO.findById(id);
        return session.isPresent() ? session.get() : null;
    }
}
