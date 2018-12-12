package com.akson.invaders.client;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Sprite extends Rectangle {

    private final GameObject gameObject;

    public Sprite(GameObject gameObject, int x, int y, int width, int height, Color color) {

        super(width, height, color);

        this.gameObject = gameObject;
        gameObject.setSprite(this);
        setTranslateX(x);
        setTranslateY(y);
    }

    void setPosition(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    public GameObject getGameObject() {
        return gameObject;
    }

}