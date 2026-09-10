package ultrathlete.com.training.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.bo.impl.SessionFileImpl;

import java.util.Collection;

@Repository
public interface FileDAO  extends CrudRepository<SessionFileImpl, Integer>  {

    @Query("SELECT f FROM SessionFileImpl f WHERE f.user.id = :userId")
    Collection<SessionFileImpl> loadFiles(int userId);
}
