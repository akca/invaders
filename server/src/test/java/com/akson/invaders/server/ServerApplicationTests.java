package com.akson.invaders.server;

import com.akson.invaders.common.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServerApplicationTests {

    private Logger logger = LoggerFactory.getLogger(ServerApplicationTests.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    void setUp() {

        TestUtils utils = new TestUtils();

        utils.registerUser("user1", restTemplate);
        utils.registerUser("user2", restTemplate);
    }

    @Test
    void register() {
        TestUtils utils = new TestUtils();

        final User user1 = utils.registerUser("user3", restTemplate);
        assertEquals(user1.getUsername(), "user3");

        final User user2 = utils.registerUser("user4", restTemplate);
        assertEquals(user2.getUsername(), "user4");
    }

    @Test
    void loginWithBasicAuth() {

        ResponseEntity<User> response1 = restTemplate.withBasicAuth("user1", "password").getForEntity("/user", User.class);

        logger.info(response1.toString());
        assertEquals(HttpStatus.OK, response1.getStatusCode());
    }

    @Test
    void failLoginWithBasicAuth() {

        ResponseEntity<User> response1 = restTemplate.withBasicAuth("user2", "wrongpass").getForEntity("/user", User.class);

        logger.info(response1.toString());
        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
    }

    @Test
    void loginWithCookies() {
        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> loginRequest;

        map1.add("username", "user1");
        map1.add("password", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        loginRequest = new HttpEntity<>(map1, headers);

        ResponseEntity<String> response1 = restTemplate.exchange("/login", HttpMethod.POST, loginRequest, String.class);
        logger.info(response1.toString());

        assertEquals(HttpStatus.OK, response1.getStatusCode());
    }

    @Test
    void failLoginWithCookies() {
        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> loginRequest;

        map1.add("username", "user1");
        map1.add("password", "wrongpass");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        loginRequest = new HttpEntity<>(map1, headers);

        ResponseEntity<String> response1 = restTemplate.exchange("/login", HttpMethod.POST, loginRequest, String.class);
        logger.info(response1.toString());

        assertEquals(HttpStatus.UNAUTHORIZED, response1.getStatusCode());
    }

}
