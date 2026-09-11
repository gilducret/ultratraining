package ultrathlete.com.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.data.UserDAO;
import ultrathlete.com.training.service.UserService;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUser(int id) {
        Optional<UserImpl> user = userDAO.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public User getUser(String auth0Id) {
        return userDAO.findByAuth0Id(auth0Id);
    }

    @Override
    public int createUser(UserImpl user) {
        UserImpl created = userDAO.save(user);
        return created != null ? created.getId() : -1;
    }

    @Override
    public Iterable<UserImpl> getAll() {
        return userDAO.findAll();
    }

    @Override
    public Collection<UserImpl> getAll(String name) {
        return userDAO.findAllUsers(name);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
