package com.akson.invaders.client.controller;

import com.akson.invaders.client.AbstractScreenController;
import com.akson.invaders.client.InvadersApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.net.URL;
import java.util.ResourceBundle;

public class Screen3Controller extends AbstractScreenController {

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
    private void goToScreen1(ActionEvent event) {
        screenManager.setScreen(InvadersApplication.mainScreenID);
    }

    @FXML
    private void goToScreen2(ActionEvent event) {
        screenManager.setScreen(InvadersApplication.gameSPID);
    }
}
