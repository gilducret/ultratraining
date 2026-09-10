package ultrathlete.com.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/{id}")
    public User findByEmail(@PathVariable int id) {

        UserImpl user = (UserImpl) userService.getUser(id);
        return user;
    }

    @GetMapping(value = "/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.getUser(email);
    }

    @GetMapping(value = "/users")
    public Iterable<UserImpl> findAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/users/{name}")
    public Collection<UserImpl> findAllUsers(@PathVariable String name) {
        return userService.getAll(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int createUser(@RequestBody UserImpl user) {
        if (user != null) {
            return userService.createUser(user);
        } else {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is null", null);
        }
    }
}
