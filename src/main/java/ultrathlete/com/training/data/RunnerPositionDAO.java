package ultrathlete.com.training.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.bo.impl.RunnerPosition;

@Repository
public interface RunnerPositionDAO
        extends CrudRepository<RunnerPosition, Integer> {
}
