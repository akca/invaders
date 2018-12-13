package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.ScreenEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the screen that shown after player dies.
 */
@Component
public class DeadEndGameSPController extends AbstractScreenController {

    @FXML
    private Label timeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void onDisplay() {

    }

    @Override
    public void onExit() {

    }

    public void setScoreLabel(double time) {

        timeLabel.setText(String.valueOf(time));
    }

    @FXML
    private void goToMainScreen(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.MAIN);
    }
}
