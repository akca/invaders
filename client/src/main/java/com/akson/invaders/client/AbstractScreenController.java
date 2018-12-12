package com.akson.invaders.client;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

public abstract class AbstractScreenController implements Initializable {

    protected ScreenManager screenManager;
    private Parent parent;

    public abstract void onDisplay();

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
