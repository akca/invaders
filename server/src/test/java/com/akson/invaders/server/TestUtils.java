package com.akson.invaders.server;

import com.akson.invaders.server.entity.User;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtils {

    public User registerUser(String name, TestRestTemplate restTemplate) {

        User user = new User();

        user.setUsername(name);
        user.setPassword("password");

        HttpEntity<User> request = new HttpEntity<>(user);

        User savedUser = restTemplate.postForObject("/register", request, User.class);

        assertEquals(name, savedUser.getUsername());

        return user;

    }

}
