package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.User;
import com.akson.invaders.server.repository.UserRepository;
import com.akson.invaders.server.util.ForbiddenException;
import com.akson.invaders.server.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

/**
 * REST controller that is responsible for user accounts.
 */
@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers User objects that sent to the given URL using HTTP POST method.
     *
     * @param user    User object to be registered
     * @param request HttpServletRequest object for fetching logged in user
     * @return persisted User object
     */
    @PostMapping(value = "/register")
    public User register(@RequestBody User user, HttpServletRequest request) {

        // TODO: password should be hashed at client side
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Updates user with the given User object using HTTP PUT method, if logged-in username is the same with
     * the User object sent to this method.
     *
     * @param user    User object to be persisted
     * @param request HttpServletRequest object for fetching logged in user
     * @return persisted User object
     */
    @PutMapping(value = "/user")
    public User updateUser(@RequestBody User user, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String loginUsername = principal.getName();

        final User loginUser = userRepository.findByUsername(loginUsername);

        if (!loginUsername.equals(user.getUsername()) || loginUser.getId() != user.getId()) {
            throw new ForbiddenException("Cannot update other users!");
        }

        // TODO: password should be hashed at client side
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Returns logged-in user details.
     *
     * @param request HttpServletRequest object for fetching logged in user
     * @return persisted User object
     */
    @GetMapping(value = "/user")
    public User getLoginUserDetails(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String loginUsername = principal.getName();

        return userRepository.findByUsername(loginUsername);
    }

    /**
     * Returns user details for given user id.
     *
     * @param id User id to fetch
     * @return persisted User object
     */
    @GetMapping(value = "/user/{id}")
    public User getUserDetails(@PathVariable Long id) {

        final Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent())
            return optionalUser.get();
        else {
            throw new NotFoundException("Cannot find a user with this id!");
        }
    }
}
