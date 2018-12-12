package com.akson.invaders.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InvadersApplication extends Application {

    public static final String mainScreenID = "main";
    public static final String gameSPID = "game_sp";
    public static final String screen3ID = "screen3";
    private static final String mainScreenFile = "view/MainScreen.fxml";
    private static final String gameSPFile = "view/GameSP.fxml";
    private static final String screen3File = "view/Screen3.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        ScreenManager controller = new ScreenManager();
        controller.loadScreen(InvadersApplication.mainScreenID, InvadersApplication.mainScreenFile);
        controller.loadScreen(InvadersApplication.gameSPID, InvadersApplication.gameSPFile);
        controller.loadScreen(InvadersApplication.screen3ID, InvadersApplication.screen3File);

        controller.setScreen(InvadersApplication.mainScreenID);

        Group root = new Group();
        root.getChildren().addAll(controller);
        Scene scene = new Scene(root);
        controller.setRootScene(scene);

        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Invaders!");
        primaryStage.show();
    }
}
