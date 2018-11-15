package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.Score;
import com.akson.invaders.server.entity.User;
import com.akson.invaders.server.repository.ScoreRepository;
import com.akson.invaders.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitConfig
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScoreControllerTest {

    private Logger logger = LoggerFactory.getLogger(ScoreControllerTest.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addScore() {
        addScores();
    }

    private void addScores() {

        scoreRepository.deleteAllInBatch();
        User user = userRepository.findByUsername("user1");

        // create scores
        Score score1 = new Score(user, 10, OffsetDateTime.now().minusHours(1));
        Score score2 = new Score(user, 80, OffsetDateTime.now().minusHours(25));
        Score score3 = new Score(user, 90, OffsetDateTime.now().minusDays(3).minusMinutes(32));
        Score score4 = new Score(user, 30, OffsetDateTime.now().minusDays(3));
        Score score5 = new Score(user, 10, OffsetDateTime.now().minusDays(5).minusHours(2));
        Score score6 = new Score(user, 70, OffsetDateTime.now().minusDays(7));
        Score score7 = new Score(user, 80, OffsetDateTime.now().minusDays(8));
        Score score8 = new Score(user, 10, OffsetDateTime.now().minusDays(40));

        postResult(score1);
        postResult(score2);
        postResult(score3);
        postResult(score4);
        postResult(score5);
        postResult(score6);
        postResult(score7);
        postResult(score8);
    }

    private void postResult(Score score) {
        ResponseEntity<Score> response1 = restTemplate.withBasicAuth("user1", "password")
                .postForEntity("/score", score, Score.class);

        logger.info(response1.toString());
        assertEquals(HttpStatus.OK, response1.getStatusCode());

    }

    @Test
    void getHighScoreDetailsAllTime() {

        addScores();

        ResponseEntity<PagedResources<Score>> response1 = restTemplate
                .withBasicAuth("user1", "password")
                .exchange("/score/all",
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                        });

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        PagedResources<Score> scoreResources = response1.getBody();

        assert scoreResources != null;
        assertEquals(8, scoreResources.getContent().size());

    }

    @Test
    void getHighScoreDetailsMonthly() {

        addScores();

        ResponseEntity<PagedResources<Score>> response1 = restTemplate
                .withBasicAuth("user1", "password")
                .exchange("/score/month",
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                        });

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        PagedResources<Score> scoreResources = response1.getBody();

        assert scoreResources != null;
        assertEquals(7, scoreResources.getContent().size());
    }

    @Test
    void getHighScoreDetailsWeekly() {

        addScores();

        ResponseEntity<PagedResources<Score>> response1 = restTemplate
                .withBasicAuth("user1", "password")
                .exchange("/score/week",
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                        });

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        PagedResources<Score> scoreResources = response1.getBody();

        assert scoreResources != null;
        assertEquals(5, scoreResources.getContent().size());

    }

    @Test
    void getHighScoreDetailsDaily() {

        addScores();

        ResponseEntity<PagedResources<Score>> response1 = restTemplate
                .withBasicAuth("user1", "password")
                .exchange("/score/day",
                        HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Score>>() {
                        });

        assertEquals(HttpStatus.OK, response1.getStatusCode());

        PagedResources<Score> scoreResources = response1.getBody();

        assert scoreResources != null;
        assertEquals(1, scoreResources.getContent().size());
    }

    @Test
    void getScoreDetails() {
    }
}