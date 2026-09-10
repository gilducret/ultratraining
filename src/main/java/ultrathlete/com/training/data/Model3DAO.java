package ultrathlete.com.training.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.service.model.Model3;

@Repository
public interface Model3DAO extends CrudRepository<Model3, Integer>  {

}
