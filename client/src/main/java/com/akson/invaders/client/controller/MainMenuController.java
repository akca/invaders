package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.ScreenEnum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Screen controller for main menu.
 */
@Component
public class MainMenuController extends AbstractScreenController {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void onDisplay() {

    }

    @Override
    public void onExit() {

    }

    @FXML
    private void goToSinglePlayer(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.GAME_SP);
    }

    @FXML
    private void goToHighScore(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.HIGHSCORE);
    }


    @FXML
    private void goToMultiPlayer(ActionEvent event) {
        screenManager.setScreen(ScreenEnum.GAME_SP);
    }
}
