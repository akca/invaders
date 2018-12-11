package com.akson.invaders.client;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public abstract class StateManager extends Thread {

    protected static Logger logger = LoggerFactory.getLogger(StateManager.class);

    protected Pane gameField;
    protected GameObject playerObject;

    protected ConcurrentHashMap<String, GameObject> gameObjects = new ConcurrentHashMap<>();

    public abstract void addObject(GameObject object);

    public abstract void moveHorizontal(String objectId, int amount);

    public abstract void moveVertical(String objectId, int amount);

    public abstract void shoot(GameObject who);

    protected Sprite findSpriteByGameObject(GameObject object) {

        for (Node n : gameField.getChildren()) {
            Sprite s = (Sprite) n;

            if (s.getGameObject() == object) {
                return s;
            }
        }

        return null;
    }

    public void deleteObject(GameObject object) {
        Platform.runLater(() ->
                gameField.getChildren().remove(object.getSprite())
        );
    }

    protected void addObjectToUI(GameObject object) {
        Platform.runLater(() ->
                gameField.getChildren().add(object.getSprite())
        );
    }

    protected void updateObject(GameObject object) {

        Platform.runLater(() -> {
            Sprite relatedSprite = findSpriteByGameObject(object);

            if (relatedSprite == null) {
                logger.error("Sprite to update cannot found!");
                return;
            }

            relatedSprite.setPosition(object.getX(), object.getY());
        });

    }


    public Pane getGameField() {
        return gameField;
    }

    public void setGameField(Pane gameField) {
        this.gameField = gameField;
    }

    public GameObject getPlayerObject() {
        return playerObject;
    }

    public void setPlayerObject(GameObject playerObject) {
        this.playerObject = playerObject;
    }
}
