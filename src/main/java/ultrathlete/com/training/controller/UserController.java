package ultrathlete.com.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/email/{email}")
    public ResponseEntity<User> findByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/auth0/{auth0Id}")
    public ResponseEntity<User> findByAuth0Id(@PathVariable String auth0Id) {
        User user = userService.getUser(auth0Id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
