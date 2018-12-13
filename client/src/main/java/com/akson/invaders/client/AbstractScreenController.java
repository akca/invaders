package com.akson.invaders.client;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

/**
 * AbstractScreenController holds common properties and methods of screen controllers.
 * All screen controllers should extend from this class.
 */
public abstract class AbstractScreenController implements Initializable {

    protected ScreenManager screenManager;
    private Parent parent;

    /**
     * Method to call before displaying a screen.
     */
    public abstract void onDisplay();

    /**
     * Method to call after exiting from a screen.
     */
    public abstract void onExit();

    public Parent getParentNode() {
        return parent;
    }

    public void setParentNode(Parent node) {
        this.parent = node;
    }

    public void setScreenManager(ScreenManager screenParent) {
        screenManager = screenParent;
    }

}
