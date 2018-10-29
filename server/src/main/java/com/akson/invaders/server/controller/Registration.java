package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.User;
import com.akson.invaders.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * REST controller that responsible for user registration.
 */
@RestController
public class Registration {

    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public Registration(UserRepository userRepository) {
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

        // TODO do checks!

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Returns list of all registered users.
     *
     * @return List of all registered users
     */
    @GetMapping(value = "/users")
    public Collection<User> listUsers() {

        return userRepository.findAll();
    }
}
