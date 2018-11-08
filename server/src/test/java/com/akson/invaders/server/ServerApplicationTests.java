package com.akson.invaders.server;

import com.akson.invaders.server.entity.User;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerApplicationTests {

    private Logger logger = LoggerFactory.getLogger(ServerApplicationTests.class);

    private final static String username1 = "testuser1";
    private final static String password1 = "password";
    private final static String username2 = "testuser2";
    private final static String password2 = "password";

    private static HttpEntity<MultiValueMap<String, String>> loginRequest1;
    private static HttpEntity<MultiValueMap<String, String>> loginRequest2;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeClass
    public static void setup() {

        MultiValueMap<String, String> map1 = new LinkedMultiValueMap<>();
        MultiValueMap<String, String> map2 = new LinkedMultiValueMap<>();


        map1.add("username", username1);
        map1.add("password", password1);

        map2.add("username", username2);
        map2.add("password", password2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        loginRequest1 = new HttpEntity<>(map1, headers);
        loginRequest2 = new HttpEntity<>(map2, headers);

    }

    @Test
    public void stage1_contextLoads() {
    }

    @Test
    public void stage2_register() {

        /* register a user */

        User user = new User();

        user.setUsername(username1);
        user.setPassword(password1);

        HttpEntity<User> request = new HttpEntity<>(user);

        user = restTemplate.postForObject("/register", request, User.class);

        assertEquals(1, user.getId());

        logger.info(user.toString());


        /* register another user */

        user = new User();

        user.setUsername(username2);
        user.setPassword(password2);

        request = new HttpEntity<>(user);

        user = restTemplate.postForObject("/register", request, User.class);

        assertEquals(2, user.getId());

        logger.info(user.toString());
    }

    @Test
    public void stage3_login() {

        HttpEntity<String> requestWithCookieHeaders1;
        HttpEntity<String> requestWithCookieHeaders2;

        /* first user log in */
        requestWithCookieHeaders1 = login(loginRequest1);

        /* second user log in */
        requestWithCookieHeaders2 = login(loginRequest2);

        logger.info("Using first user:");

        ResponseEntity<String> response1 = restTemplate.exchange("/user", HttpMethod.GET, requestWithCookieHeaders1, String.class);

        logger.info(response1.toString());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        logger.info("Using second user:");

        ResponseEntity<String> response2 = restTemplate.exchange("/user", HttpMethod.GET, requestWithCookieHeaders2, String.class);

        logger.info(response2.toString());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }

    @Test
    public void stage4_enterLobby() {
        ResponseEntity<String> response;

        response = restTemplate.exchange("/enterlobby", HttpMethod.GET, login(loginRequest1), String.class);
        logger.info(response.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        logger.info("User1 entered the lobby successfully!");


        response = restTemplate.exchange("/enterlobby", HttpMethod.GET, login(loginRequest2), String.class);
        logger.info(response.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        logger.info("User2 entered the lobby successfully!");


        response = restTemplate.exchange("/lobby", HttpMethod.GET, login(loginRequest2), String.class);
        logger.info(response.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    /**
     * Takes request entity with username & password form data, sends them to /login.
     *
     * @param loginRequest request with username & password form data
     * @return request entity with authentication cookie header
     */
    private HttpEntity<String> login(HttpEntity<MultiValueMap<String, String>> loginRequest) {

        ResponseEntity<String> response1 = restTemplate.exchange("/login", HttpMethod.POST, loginRequest, String.class);
        logger.info(response1.toString());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", response1.getHeaders().getFirst(HttpHeaders.SET_COOKIE));

        logger.info("User logged in successfully!");

        return new HttpEntity<>(requestHeaders);
    }
}
