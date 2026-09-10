package ultrathlete.com.training.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.bo.PredictedSession;

import java.util.Collection;

@Repository
public interface PredictedSessionDAO extends CrudRepository<PredictedSession, Integer>  {

    @Query("SELECT p FROM PredictedSession p WHERE p.user.id = :userId")
    Collection<PredictedSession> loadPredictedessions(int userId);
}
