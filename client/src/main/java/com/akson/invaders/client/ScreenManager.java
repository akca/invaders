package com.akson.invaders.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Holds screens, manages screen state, provides screen switching functionality.
 */
@Component
public class ScreenManager extends StackPane implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ScreenManager.class);

    /**
     * Holds all loaded screens.
     */
    private final HashMap<ScreenEnum, AbstractScreenController> screens = new HashMap<>();

    private Scene rootScene;
    private AbstractScreenController currentScreen;
    private ApplicationContext applicationContext;

    /**
     * Add the screen to the collection.
     *
     * @param screenEnum Enum of the screen
     * @param controller Screen screenManager to be added
     */
    private void addScreen(ScreenEnum screenEnum, AbstractScreenController controller) {
        screens.put(screenEnum, controller);
    }

    /**
     * Load FXML file, initialize it, and store it inside the screens map.
     *
     * @param screenEnum enum of the screen that is loaded
     * @param resource   path of the FXML file
     */
    public void loadScreen(ScreenEnum screenEnum, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            loader.setControllerFactory(applicationContext::getBean);

            Parent loadScreen = loader.load();

            AbstractScreenController screenController = loader.getController();
            screenController.setScreenManager(this);
            screenController.setParentNode(loadScreen);
            addScreen(screenEnum, screenController);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Change current screen to the screen with given name.
     *
     * @param screenEnum enum of the screen to be switched.
     */
    public void setScreen(ScreenEnum screenEnum) {

        AbstractScreenController newScreen = screens.get(screenEnum);

        if (newScreen != null) {

            if (currentScreen != null) {
                currentScreen.onExit();
            }

            // remove the current screen
            getChildren().clear();

            currentScreen = newScreen;
            getChildren().add(currentScreen.getParentNode());
            currentScreen.onDisplay();

        } else {
            logger.error("Screen with name {} is not available!", screenEnum);
        }
    }

    public Scene getRootScene() {
        return rootScene;
    }

    public void setRootScene(Scene rootScene) {
        this.rootScene = rootScene;
    }

    public AbstractScreenController getCurrentScreen() {
        return currentScreen;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

