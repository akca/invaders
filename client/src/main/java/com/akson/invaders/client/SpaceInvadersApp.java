package com.akson.invaders.client;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.scene.input.KeyCode.*;

public class SpaceInvadersApp extends Application {

    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 800;
    public static final int PLAYER_SIZE = 40;
    public static final int ENEMY_SIZE = 30;

    private static final Logger logger = LoggerFactory.getLogger(SpaceInvadersApp.class);

    private Stage stage;
    private Pane root = new Pane();

    private StateManager stateManager;

    private BooleanProperty upPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setResizable(false);

        switchToMainMenuScene();

    }

    private Parent createMainMenuUI() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        grid.setAlignment(Pos.CENTER);

        DropShadow ds = new DropShadow();
        ds.setRadius(15);
        ds.setSpread(0.99);
        ds.setColor(Color.color(0.1, 0.1, 0.1));

        Text header = new Text("INVADERS!");
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 70));
        header.setFill(Color.RED);
        header.setEffect(ds);

        Button singlePlayerButton = new Button("Singleplayer");
        Button multiPlayerButton = new Button("Multiplayer");
        Button highScoresButton = new Button("Highscores");
        Button exitButton = new Button("Exit");

        singlePlayerButton.setMinWidth(300);
        multiPlayerButton.setMinWidth(300);
        highScoresButton.setMinWidth(300);
        exitButton.setMinWidth(300);

        GridPane.setHalignment(singlePlayerButton, HPos.CENTER);
        GridPane.setHalignment(multiPlayerButton, HPos.CENTER);
        GridPane.setHalignment(highScoresButton, HPos.CENTER);
        GridPane.setHalignment(exitButton, HPos.CENTER);

        singlePlayerButton.setEffect(ds);
        multiPlayerButton.setEffect(ds);
        highScoresButton.setEffect(ds);
        exitButton.setEffect(ds);

        singlePlayerButton.setOnAction(e -> switchToGameScene());

        exitButton.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        grid.add(header, 0, 0);
        grid.add(singlePlayerButton, 0, 6);
        grid.add(multiPlayerButton, 0, 7);
        grid.add(highScoresButton, 0, 8);
        grid.add(exitButton, 0, 9);

        return grid;
    }

    private void switchToMainMenuScene() {

        Scene mainScene = new Scene(createMainMenuUI());

        stage.setScene(mainScene);
        stage.show();
    }

    private void switchToGameScene() {

        Scene gameScene = new Scene(root);
        stateManager = new StateManagerSP();
        stateManager.setGameField(root);

        GameObject playerObject = new GameObject(GameObjectType.LOCAL_PLAYER, 300, 700);

        stateManager.setPlayerObject(playerObject);

        Sprite playerSprite = new Sprite(playerObject,
                playerObject.getX(),
                playerObject.getY(),
                PLAYER_SIZE,
                PLAYER_SIZE,
                Color.BLUE);

        stateManager.addObject(playerObject);


        gameScene.setOnKeyPressed(e -> {
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

        gameScene.setOnKeyReleased(e -> {
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

        createAliens();
        timer.start();
        stateManager.start();

        stage.setScene(gameScene);
        stage.show();

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
}