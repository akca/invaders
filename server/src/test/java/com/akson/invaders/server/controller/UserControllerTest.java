package com.akson.invaders.server.controller;

import com.akson.invaders.server.TestUtils;
import com.akson.invaders.server.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void register() {
        TestUtils utils = new TestUtils();

        final User user1 = utils.registerUser("userA", restTemplate);
        assertEquals(user1.getUsername(), "userA");

        final User user2 = utils.registerUser("userB", restTemplate);
        assertEquals(user2.getUsername(), "userB");
    }

    @Test
    void updateUser() {
        ResponseEntity<User> entity1 = restTemplate.withBasicAuth("userA", "password")
                .getForEntity("/user", User.class);
        assertEquals(HttpStatus.OK, entity1.getStatusCode());

        // update points with 789
        final User user1 = entity1.getBody();
        Objects.requireNonNull(user1).setPoints(789);

        // send PUT request to update user
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> requestEntity = new HttpEntity<>(user1, headers);
        ResponseEntity<User> response = restTemplate.withBasicAuth("userA", "password")
                .exchange("/user", HttpMethod.PUT, requestEntity, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getPoints(), 789);
    }

    @Test
    void getLoginUserDetails() {
        ResponseEntity<User> entity1 = restTemplate.withBasicAuth("user1", "password")
                .getForEntity("/user", User.class);

        assertEquals(HttpStatus.OK, entity1.getStatusCode());
        assertEquals(Objects.requireNonNull(entity1.getBody()).getUsername(), "user1");
    }

    @Test
    void getLoginUserDetails401() {
        ResponseEntity<User> entity1 = restTemplate.withBasicAuth("none", "wrongpass")
                .getForEntity("/user", User.class);

        assertEquals(HttpStatus.UNAUTHORIZED, entity1.getStatusCode());
    }

    @Test
    void getUserDetails() {
        ResponseEntity<User> entity1 = restTemplate.withBasicAuth("user1", "password")
                .getForEntity("/user/1", User.class);

        assertEquals(HttpStatus.OK, entity1.getStatusCode());
        assertEquals(Objects.requireNonNull(entity1.getBody()).getUsername(), "user1");
    }

    @Test
    void getUserDetails404() {
        ResponseEntity<User> entity1 = restTemplate.withBasicAuth("user1", "password")
                .getForEntity("/user/165646", User.class);

        assertEquals(HttpStatus.NOT_FOUND, entity1.getStatusCode());
    }
}