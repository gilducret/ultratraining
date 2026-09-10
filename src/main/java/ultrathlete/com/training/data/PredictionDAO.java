package ultrathlete.com.training.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.Prediction;

import java.util.Collection;


@Repository
public interface PredictionDAO extends CrudRepository<Prediction, Integer>  {
    @Query("SELECT p FROM Prediction p WHERE p.session.id = :sessionId order by p.distance")
    Collection<Prediction> loadPredictions(int sessionId);
}
