package com.akson.invaders.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.scene.input.KeyCode.*;

public class SpaceInvadersApp extends Application {

    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 800;
    public static final int PLAYER_SIZE = 40;
    public static final int ENEMY_SIZE = 30;
    private static Logger logger = LoggerFactory.getLogger(SpaceInvadersApp.class);
    private Pane root = new Pane();
    private double t = 0;

    private GameObject playerObject;
    private StateManager stateManager;

    private BooleanProperty upPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();

    public static void main(String[] args) {
        launch(args);
    }

    private Parent createContent() {

        stateManager = new StateManagerSP(this);

        playerObject = new GameObject(GameObjectType.LOCAL_PLAYER, 300, 700);

        Sprite playerSprite = new Sprite(playerObject,
                playerObject.getX(),
                playerObject.getY(),
                PLAYER_SIZE,
                PLAYER_SIZE,
                Color.BLUE);

        stateManager.addObject(playerObject);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (leftPressed.get() && playerObject.getX() > 0) {
                    stateManager.moveHorizontal(playerObject.getId(), -4);
                }
                if (rightPressed.get() && playerObject.getX() + PLAYER_SIZE < root.getWidth()) {
                    stateManager.moveHorizontal(playerObject.getId(), 4);
                }
                if (upPressed.get() && playerObject.getY() > 0) {
                    stateManager.moveVertical(playerObject.getId(), -4);
                }
                if (downPressed.get() && playerObject.getY() + PLAYER_SIZE < root.getHeight()) {
                    stateManager.moveVertical(playerObject.getId(), 4);
                }
                if (spacePressed.get()) {
                    stateManager.shoot(playerObject);
                }
            }
        };

        timer.start();
        createAliens();

        return root;
    }
//
//    private List<Sprite> sprites() {
//        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
//    }

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

    @Override
    public void start(Stage stage) throws Exception {

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setResizable(false);

        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
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

        scene.setOnKeyReleased(e -> {
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

        stage.setScene(scene);
        stage.show();
        stateManager.start();
    }

    private Sprite findSpriteByGameObject(GameObject object) {

        for (Node n : root.getChildren()) {
            Sprite s = (Sprite) n;

            if (s.getGameObject() == object) {
                return s;
            }
        }

        return null;
    }

    public void updateObject(GameObject object) {

        Platform.runLater(() -> {
            Sprite relatedSprite = findSpriteByGameObject(object);

            if (relatedSprite == null) {
                logger.error("Sprite to update cannot found!");
                return;
            }

            relatedSprite.setPosition(object.getX(), object.getY());
        });

    }

    public void addObject(GameObject object) {
        Platform.runLater(() ->
                root.getChildren().add(object.getSprite())
        );
    }

    public void deleteObject(GameObject object) {
        Platform.runLater(() ->
                root.getChildren().remove(object.getSprite())
        );
    }

    public GameObject getPlayerObject() {
        return playerObject;
    }
}