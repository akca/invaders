package com.akson.invaders.client;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class StateManager implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(StateManager.class);
    protected final AtomicBoolean running = new AtomicBoolean(false);
    protected final ConcurrentHashMap<String, GameObject> gameObjects = new ConcurrentHashMap<>();
    protected GameObject playerObject;
    protected Pane gameFieldPane;
    private Thread workerThread = null;

    public abstract void addObject(GameObject object);

    public abstract void moveHorizontal(String objectId, int amount);

    public abstract void moveVertical(String objectId, int amount);

    public abstract void shoot(GameObject who);

    protected Sprite findSpriteByGameObject(GameObject object) {

        for (Node n : gameFieldPane.getChildren()) {
            Sprite s = (Sprite) n;

            if (s.getGameObject() == object) {
                return s;
            }
        }

        return null;
    }

    public void deleteObject(GameObject object) {
        Platform.runLater(() ->
                gameFieldPane.getChildren().remove(object.getSprite())
        );
    }

    protected void addObjectToUI(GameObject object) {
        Platform.runLater(() ->
                gameFieldPane.getChildren().add(object.getSprite())
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


    public Pane getGameFieldPane() {
        return gameFieldPane;
    }

    public void setGameFieldPane(Pane gameFieldPane) {
        this.gameFieldPane = gameFieldPane;
    }

    public GameObject getPlayerObject() {
        return playerObject;
    }

    public void setPlayerObject(GameObject playerObject) {
        this.playerObject = playerObject;
    }

    public void start() {
        workerThread = new Thread(this);
        workerThread.setName("StateManager Thread");
        workerThread.start();
    }

    public void interrupt() {
        running.set(false);

        if (workerThread != null) {
            workerThread.interrupt();
        }
    }


    boolean isRunning() {
        return running.get();
    }
}
