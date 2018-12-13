package com.akson.invaders.client;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Main class of the Invaders Game Client.
 */
@SpringBootApplication
public class InvadersApplication extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(InvadersApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        ScreenManager controller = springContext.getBean(ScreenManager.class);
        controller.loadScreen(ScreenEnum.LOGIN, "view/Login.fxml");
        controller.loadScreen(ScreenEnum.REGISTER, "view/Register.fxml");
        controller.loadScreen(ScreenEnum.MAIN, "view/MainMenu.fxml");
        controller.loadScreen(ScreenEnum.GAME_SP, "view/GameSP.fxml");
        controller.loadScreen(ScreenEnum.GAME_END_SP, "view/EndGameSP.fxml");
        controller.loadScreen(ScreenEnum.GAME_DEAD_END_SP, "view/DeadEndGameSP.fxml");
        controller.loadScreen(ScreenEnum.HIGHSCORE, "view/Highscore.fxml");

        controller.setScreen(ScreenEnum.LOGIN);

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
