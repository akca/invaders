package com.akson.invaders.client.controller;

import com.akson.invaders.client.*;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.*;


public class GameSPController extends AbstractScreenController {

    private static final int PLAYER_SIZE = 40;
    private static final int ENEMY_SIZE = 30;

    private final BooleanProperty upPressed = new SimpleBooleanProperty();
    private final BooleanProperty downPressed = new SimpleBooleanProperty();
    private final BooleanProperty rightPressed = new SimpleBooleanProperty();
    private final BooleanProperty leftPressed = new SimpleBooleanProperty();
    private final BooleanProperty spacePressed = new SimpleBooleanProperty();

    @FXML
    private Pane gameFieldPane;

    private StateManager stateManager = null;
    private AnimationTimer timer;
    private Date latestShootTime = new Date();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void onDisplay() {

        stateManager = new StateManagerSP();
        stateManager.setGameFieldPane(gameFieldPane);

        GameObject playerObject = new GameObject(GameObjectType.LOCAL_PLAYER, 300, 700);

        Sprite playerSprite = new Sprite(playerObject,
                playerObject.getX(),
                playerObject.getY(),
                PLAYER_SIZE,
                PLAYER_SIZE,
                Color.BLUE);

        stateManager.addObject(playerObject);
        stateManager.setPlayerObject(playerObject);

        screenManager.getRootScene().setOnKeyPressed(e -> {
            if (e.getCode() == UP) {
                upPressed.set(true);
            }
            if (e.getCode() == DOWN) {
                downPressed.set(true);
            }
            if (e.getCode() == LEFT) {
                leftPressed.set(true);
            }
            if (e.getCode() == RIGHT) {
                rightPressed.set(true);
            }
            if (e.getCode() == SPACE) {
                spacePressed.set(true);
            }
        });

        screenManager.getRootScene().setOnKeyReleased(e -> {
            if (e.getCode() == UP) {
                upPressed.set(false);
            }
            if (e.getCode() == DOWN) {
                downPressed.set(false);
            }
            if (e.getCode() == LEFT) {
                leftPressed.set(false);
            }
            if (e.getCode() == RIGHT) {
                rightPressed.set(false);
            }
            if (e.getCode() == SPACE) {
                spacePressed.set(false);
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (leftPressed.get() && playerObject.getX() > 0) {
                    stateManager.moveHorizontal(playerObject.getId(), -4);
                }
                if (rightPressed.get() && playerObject.getX() + PLAYER_SIZE < gameFieldPane.getWidth()) {
                    stateManager.moveHorizontal(playerObject.getId(), 4);
                }
                if (upPressed.get() && playerObject.getY() > 0) {
                    stateManager.moveVertical(playerObject.getId(), -4);
                }
                if (downPressed.get() && playerObject.getY() + PLAYER_SIZE < gameFieldPane.getHeight()) {
                    stateManager.moveVertical(playerObject.getId(), 4);
                }
                if (spacePressed.get()) {

                    Date currentTime = new Date();

                    if (currentTime.getTime() - latestShootTime.getTime() > 200) {
                        stateManager.shoot(playerObject);
                        latestShootTime = currentTime;
                    }

                }
            }
        };

        createAliens();
        stateManager.start();
        timer.start();
    }

    @Override
    public void onExit() {

        if (stateManager != null) {
            stateManager.interrupt();
        }

        if (timer != null) {
            timer.stop();
        }

        gameFieldPane.getChildren().clear();
    }

    private void createAliens() {

        for (int i = 0; i < 5; i++) {

            GameObject alienObject = new GameObject(GameObjectType.ENEMY, 90 + i * 100, 100);

            Sprite alienSprite = new Sprite(alienObject,
                    alienObject.getX(),
                    alienObject.getY(),
                    ENEMY_SIZE,
                    ENEMY_SIZE,
                    Color.RED);

            stateManager.addObject(alienObject);

        }
    }

    @FXML
    private void goToMainScreen(ActionEvent event) {
        screenManager.setScreen(InvadersApplication.mainScreenID);
    }

    public Pane getGameFieldPane() {
        return gameFieldPane;
    }
}
