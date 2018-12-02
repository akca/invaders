package com.akson.invaders.server.controller;

import com.akson.invaders.common.entity.Match;
import com.akson.invaders.common.entity.attr.MatchState;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LobbyTest {

    private Logger logger = LoggerFactory.getLogger(LobbyTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void enterLobby() {

        // first user enters lobby
        ResponseEntity<Match> response = restTemplate.withBasicAuth("user1", "password").getForEntity("/enterlobby", Match.class);
        logger.info(response.toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        logger.info("User1 entered the lobby successfully!");

        // second user enters lobby
        response = restTemplate.withBasicAuth("user2", "password").getForEntity("/enterlobby", Match.class);
        logger.info(response.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // we should get a Match object at response body when second user enters to the lobby
        assertEquals(MatchState.LOBBY, Objects.requireNonNull(response.getBody()).getState());
        logger.info("User2 entered the lobby successfully!");

        Match match = response.getBody();

        // when first user sends another request, it should get the same Match object
        response = restTemplate.withBasicAuth("user1", "password").getForEntity("/enterlobby", Match.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(match, response.getBody());

        // when second user sends another request, it should get the same Match object
        response = restTemplate.withBasicAuth("user2", "password").getForEntity("/enterlobby", Match.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(match, response.getBody());

    }

    @Test
    @Disabled
    void leaveLobby() {
        // TODO: implement this
    }

    @Test
    @Disabled
    void listLobby() {
        // TODO: implement this
    }
}