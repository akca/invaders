package com.akson.invaders.client.controller;

import com.akson.invaders.client.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.*;

/**
 * Controller for the single player game view.
 */
@Component
public class GameSPController extends AbstractScreenController {

    private static final int PLAYER_SIZE = 40;
    private static final int ENEMY_SIZE = 30;
    private static final Logger logger = LoggerFactory.getLogger(GameSPController.class);

    private final BooleanProperty upPressed = new SimpleBooleanProperty();
    private final BooleanProperty downPressed = new SimpleBooleanProperty();
    private final BooleanProperty rightPressed = new SimpleBooleanProperty();
    private final BooleanProperty leftPressed = new SimpleBooleanProperty();
    private final BooleanProperty spacePressed = new SimpleBooleanProperty();

    private int level = 0;

    @FXML
    private Label gameScore;

    @FXML
    private Pane gameFieldPane;

    private StateManager stateManager = null;
    private AnimationTimer timer;
    private Date latestShootTime = new Date();
    private Date latestVerticalMoveTime = new Date();
    private Date latestHorizontalMoveTime = new Date();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @Override
    public void onDisplay() {

        level = 0;

        // create StateManager
        stateManager = new StateManagerSP();
        stateManager.setGameFieldPane(gameFieldPane);
        stateManager.setGameScore(gameScore);
        stateManager.setScreenController(this);

        GameObject playerObject = new GameObject(GameObjectType.LOCAL_PLAYER, 300, 700);

        Sprite playerSprite = new Sprite(playerObject,
                playerObject.getX(),
                playerObject.getY(),
                PLAYER_SIZE,
                PLAYER_SIZE,
                Color.BLUE);

        stateManager.addObject(playerObject);
        stateManager.setPlayerObject(playerObject);

        // register keypress actions
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

        /*
         * Handle keypress events.
         * Important thing is, key events are limited for specific time intervals.
         * In 30 ms, player can only move ONCE in horizontal direction.
         * In 30 ms, player can only move ONCE in vertical direction.
         * In 200 ms, player can only shoot ONCE.
         */
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (leftPressed.get() && playerObject.getX() > 0) {
                    Date currentTime = new Date();

                    if (currentTime.getTime() - latestHorizontalMoveTime.getTime() > 30) {
                        stateManager.moveHorizontal(playerObject.getId(), -6);
                        latestHorizontalMoveTime = currentTime;
                    }
                }

                if (rightPressed.get() && playerObject.getX() + PLAYER_SIZE < gameFieldPane.getWidth()) {
                    Date currentTime = new Date();

                    if (currentTime.getTime() - latestHorizontalMoveTime.getTime() > 30) {
                        stateManager.moveHorizontal(playerObject.getId(), 6);
                        latestHorizontalMoveTime = currentTime;
                    }
                }

                if (upPressed.get() && playerObject.getY() > 0) {
                    Date currentTime = new Date();

                    if (currentTime.getTime() - latestVerticalMoveTime.getTime() > 30) {
                        stateManager.moveVertical(playerObject.getId(), -6);
                        latestVerticalMoveTime = currentTime;
                    }
                }

                if (downPressed.get() && playerObject.getY() + PLAYER_SIZE < gameFieldPane.getHeight()) {
                    Date currentTime = new Date();

                    if (currentTime.getTime() - latestVerticalMoveTime.getTime() > 30) {
                        stateManager.moveVertical(playerObject.getId(), 6);
                        latestVerticalMoveTime = currentTime;
                    }
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

        nextLevel(0);
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

    /**
     * Create aliens for given level.
     * Number of the aliens are calculated using this formula: (level * 2) + 5
     * Then, aliens are evenly distributed along the screen.
     *
     * @param level level number (0-indexed)
     */
    private void createAliens(int level) {

        int enemyCount = (level * 2) + 5;
        int margin = 60;

        for (int i = 0; i < enemyCount; i++) {

            // evenly distribute enemies
            int xLocation = margin
                    + i * (((int) gameFieldPane.getMinWidth() - (ENEMY_SIZE + margin) - margin) / (enemyCount - 1));

            GameObject alienObject = new GameObject(GameObjectType.ENEMY, xLocation, 100);

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
        screenManager.setScreen(ScreenEnum.MAIN);
    }

    /**
     * Player died. Go to "wasted" i.e. ScreenEnum.GAME_DEAD_END_SP screen.
     *
     * @param time elapsed time.
     */
    public void gameDeadEnd(double time) {
        Platform.runLater(() -> {
            screenManager.setScreen(ScreenEnum.GAME_DEAD_END_SP);
            DeadEndGameSPController endController = (DeadEndGameSPController) screenManager.getCurrentScreen();

            endController.setScoreLabel(time);
        });
    }

    /**
     * Player killed all enemies. Go to "End" i.e. ScreenEnum.GAME_END_SP screen.
     *
     * @param time elapsed time.
     */
    private void gameEnd(double time) {
        Platform.runLater(() -> {
            screenManager.setScreen(ScreenEnum.GAME_END_SP);
            EndGameSPController endController = (EndGameSPController) screenManager.getCurrentScreen();

            endController.sendHighScore(time);
        });
    }

    /**
     * Go to next level, if current level is not the final one. Otherwise, finish the game.
     *
     * @param time elapsed time
     */
    public void nextLevel(double time) {
        if (level <= 2) {
            createAliens(level++);
        } else {
            gameEnd(time);
        }
    }
}
