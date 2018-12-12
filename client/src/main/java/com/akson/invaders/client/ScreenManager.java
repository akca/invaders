package com.akson.invaders.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class ScreenManager extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(ScreenManager.class);
    private final HashMap<String, AbstractScreenController> screens = new HashMap<>();
    private Scene rootScene;
    private AbstractScreenController currentScreen;

    /**
     * Add the screen to the collection.
     *
     * @param name       Name of the screen
     * @param controller Screen screenManager to be added
     */
    private void addScreen(String name, AbstractScreenController controller) {
        screens.put(name, controller);
    }

    public void loadScreen(String name, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = loader.load();

            AbstractScreenController screenController = loader.getController();
            screenController.setScreenManager(this);
            screenController.setParentNode(loadScreen);
            addScreen(name, screenController);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Change current screen to the screen with given name.
     *
     * @param name name of the screen to be switched.
     */
    public void setScreen(final String name) {

        AbstractScreenController newScreen = screens.get(name);

        if (newScreen != null) {

            if (currentScreen != null) {
                currentScreen.onExit();
            }

            // remove the current screen
            getChildren().clear();

            currentScreen = newScreen;
            currentScreen.onDisplay();
            getChildren().add(currentScreen.getParentNode());

        } else {
            logger.error("Screen with name {} is not available!", name);
        }
    }

    public Scene getRootScene() {
        return rootScene;
    }

    public void setRootScene(Scene rootScene) {
        this.rootScene = rootScene;
    }
}

