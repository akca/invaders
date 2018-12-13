package com.akson.invaders.client;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StateManager is the cornerstone of the game. Manages all {@link GameObject}'s, game state,
 * UI objects, score and all others.
 * <p>
 * This abstract StateManager provides common functionality of single player and multi player game modes.
 * Specific behaviours of these game modes are implemented inside {@link StateManagerSP} and {@link StateManagerMP}.
 * <p>
 * StateManager runs in its own thread. Therefore, it does not block UI thread.
 */
public abstract class StateManager implements Runnable {

    protected static final Logger logger = LoggerFactory.getLogger(StateManager.class);
    protected final AtomicBoolean running = new AtomicBoolean(false);
    protected final ConcurrentHashMap<String, GameObject> gameObjects = new ConcurrentHashMap<>();
    protected GameObject playerObject;
    protected Pane gameFieldPane;
    protected Label gameScore;
    protected AbstractScreenController screenController;
    private Thread workerThread = null;

    public abstract void addObject(GameObject object);

    public abstract void moveHorizontal(String objectId, int amount);

    public abstract void moveVertical(String objectId, int amount);

    public abstract void shoot(GameObject who);

    /**
     * Find a GameObject's sprite from all added Sprites.
     *
     * @param object object whose sprite is going to be found
     * @return Sprite of the object OR null (if not found).
     */
    protected Sprite findSpriteByGameObject(GameObject object) {

        for (Node n : gameFieldPane.getChildren()) {
            Sprite s = (Sprite) n;

            if (s.getGameObject() == object) {
                return s;
            }
        }

        return null;
    }

    /**
     * Returns a list of GameObject whose type is same with the given type.
     *
     * @param type filter type
     * @return filtered list of GameObjects
     */
    protected List<GameObject> filterObjectsByType(GameObjectType type) {
        List<GameObject> result = new ArrayList<>();

        for (GameObject o : gameObjects.values()) {
            if (o.getType() == type && !o.isDead()) {
                result.add(o);
            }
        }

        return result;
    }

    /**
     * Delete an object from UI.
     *
     * @param object object to delete
     */
    public void deleteObject(GameObject object) {
        Platform.runLater(() ->
                gameFieldPane.getChildren().remove(object.getSprite())
        );
    }

    /**
     * Add an object to UI.
     *
     * @param object object to delete
     */
    protected void addObjectToUI(GameObject object) {
        Platform.runLater(() ->
                gameFieldPane.getChildren().add(object.getSprite())
        );
    }

    /**
     * Update an object from UI.
     *
     * @param object object to delete
     */
    protected void updateObject(GameObject object) {

        Platform.runLater(() -> {
            Sprite relatedSprite = findSpriteByGameObject(object);

            if (relatedSprite == null) {
                logger.debug("Sprite to update cannot found!");
                return;
            }

            relatedSprite.setPosition(object.getX(), object.getY());
        });
    }

    /**
     * Start StateManager thread.
     */
    public void start() {
        workerThread = new Thread(this);
        workerThread.setName("StateManager Thread");
        workerThread.start();
    }

    /**
     * Interrupt StateManager Thread.
     */
    public void interrupt() {
        running.set(false);

        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    public boolean isRunning() {
        return running.get();
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

    public Label getGameScore() {
        return gameScore;
    }

    public void setGameScore(Label gameScore) {
        this.gameScore = gameScore;
    }

    public AbstractScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(AbstractScreenController screenController) {
        this.screenController = screenController;
    }
}
