package ultrathlete.com.training.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.service.model.Model2;

@Repository
public interface Model2DAO extends CrudRepository<Model2, Integer>  {

}
