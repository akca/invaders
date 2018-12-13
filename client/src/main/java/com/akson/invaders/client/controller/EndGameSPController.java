package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.ScreenEnum;
import com.akson.invaders.common.entity.Score;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ResourceBundle;

/**
 * Controller for the screen that shown after game finishes, i.e. player kills all enemies.
 */
@Component
public class EndGameSPController extends AbstractScreenController {

    @FXML
    private Label timeLabel;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void onDisplay() {

    }

    @Override
    public void onExit() {

    }

    void sendHighScore(double time) {

        timeLabel.setText(String.valueOf(time));

        Score score = new Score(LoginController.currentUser, time, OffsetDateTime.now());

        ResponseEntity<Score> response1 = restTemplate.postForEntity("http://localhost:8080/score", score, Score.class);

    }

    @FXML
    private void goToMainScreen(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.MAIN);
    }

    @FXML
    private void goToHighScore(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.HIGHSCORE);
    }
}
