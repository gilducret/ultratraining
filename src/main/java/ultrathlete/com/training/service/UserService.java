package ultrathlete.com.training.service;

import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.bo.impl.UserImpl;

import java.util.Collection;
import java.util.List;

public interface UserService {
    User getUser(int id);
    User getUser(String auth0Id);
    int createUser(UserImpl user);
    Iterable<UserImpl> getAll();
    Collection<UserImpl> getAll(String name);
    User getUserByEmail(String email);
}
