package ultrathlete.com.training.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ultrathlete.com.training.bo.impl.UserImpl;

import java.util.Collection;

@Repository
public interface UserDAO extends  CrudRepository<UserImpl, Integer> {

    UserImpl findByEmail(String email);

    @Query("SELECT u FROM UserImpl u WHERE (u.firstName like :name% or u.lastName like :name%) order by u.lastName, u.firstName")
    Collection<UserImpl> findAllUsers(String name);
}
