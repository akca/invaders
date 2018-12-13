package com.akson.invaders.client;

import com.akson.invaders.client.controller.GameSPController;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Specialized {@link StateManager} for single player game mode.
 *
 * @see StateManager
 */
public class StateManagerSP extends StateManager {

    private final int bulletWidth = 5;
    private final int bulletHeight = 15;
    private Date gameStartTime;

    /**
     * Add a GameObject to game.
     *
     * @param object GameObject to add
     */
    @Override
    public void addObject(GameObject object) {
        gameObjects.put(object.getId(), object);

        addObjectToUI(object);
    }

    /**
     * Move given object horizontally by given amount.
     *
     * @param objectId object to move
     * @param amount   amount of move
     */
    @Override
    public void moveHorizontal(String objectId, int amount) {
        GameObject gameObject = gameObjects.get(objectId);

        int newX = gameObject.getX() + amount;
        gameObject.setX(newX);

        updateObject(gameObject);
    }

    /**
     * Move given object vertically by given amount.
     *
     * @param objectId object to move
     * @param amount   amount of move
     */
    @Override
    public void moveVertical(String objectId, int amount) {
        GameObject gameObject = gameObjects.get(objectId);

        int newY = gameObject.getY() + amount;
        gameObject.setY(newY);

        updateObject(gameObject);
    }

    /**
     * Traverse all GameObjects and update their states in every game tick.
     * <p>
     * Executed in StateManager thread.
     *
     * @see Runnable#run()
     */
    @Override
    public void run() {

        gameStartTime = new Date();
        running.set(true);

        while (running.get()) {

            // Calculate elapsedSeconds and show it in UI
            double elapsedSeconds = (new Date().getTime() - gameStartTime.getTime()) / 1000d;
            Platform.runLater(() -> gameScore.setText(String.valueOf(elapsedSeconds)));

            // traverse all GameObjects and update their states
            gameObjects.values().forEach(gameObject -> {

                switch (gameObject.getType()) {

                    case ENEMY_BULLET:
                        moveVertical(gameObject.getId(), 7);

                        Bounds bulletBounds = gameObject.getSprite().getBoundsInParent();
                        Bounds playerBounds = playerObject.getSprite().getBoundsInParent();

                        if (bulletBounds.intersects(playerBounds)) {

                            // decrement health by 20
                            playerObject.setHealth(playerObject.getHealth() - 20);

                            if (playerObject.getHealth() <= 0) {
                                playerObject.setDead(true);

                                logger.info("You are dead!");
                                running.set(false);

                                // player dead, go to game end screen
                                ((GameSPController) screenController).gameDeadEnd(elapsedSeconds);

                                return;
                            }

                            gameObject.setDead(true);
                        }
                        break;

                    case BULLET:
                        moveVertical(gameObject.getId(), -7);

                        gameObjects.values().stream()
                                .filter(e -> e.getType().equals(GameObjectType.ENEMY))
                                .forEach(enemy -> {
                                    if (gameObject.getSprite().getBoundsInParent()
                                            .intersects(enemy.getSprite().getBoundsInParent())) {

                                        // decrement health by 20
                                        enemy.setHealth(enemy.getHealth() - 20);

                                        if (enemy.getHealth() <= 0) {
                                            enemy.setDead(true);

                                            List<GameObject> enemies = filterObjectsByType(GameObjectType.ENEMY);

                                            // if no alive enemies left, go to next level!
                                            if (enemies.isEmpty()) {
                                                ((GameSPController) screenController).nextLevel(elapsedSeconds);
                                            }
                                        }

                                        gameObject.setDead(true);
                                    }
                                });

                        break;

                    case ENEMY:

                        // call shoot() by enemies randomly
                        if (Math.random() < 0.02) {
                            shoot(gameObject);
                        }

                        break;
                }
            });

            /* remove dead or out-of-the-screen objects */
            Iterator<GameObject> it = gameObjects.values().iterator();

            while (it.hasNext()) {

                GameObject gameObject = it.next();

                if (gameObject.isDead() || gameObject.getY() > gameFieldPane.getMinHeight() || gameObject.getY() < 0) {

                    deleteObject(gameObject);
                    it.remove();
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                interrupt();
                logger.debug("StateManager thread is interrupted");
            }
        }
    }

    /**
     * Create a bullet object.
     *
     * @param who owner of the bullet
     */
    @Override
    public void shoot(GameObject who) {

        GameObjectType bulletType;

        if (who.getType().equals(GameObjectType.ENEMY)) {
            bulletType = GameObjectType.ENEMY_BULLET;
        } else {
            bulletType = GameObjectType.BULLET;
        }

        // bullet goes from the center of the sprite
        int positionX = who.getX() + (int) ((who.getSprite().getWidth() - bulletWidth) / 2);

        GameObject bulletObject = new GameObject(bulletType, positionX, who.getY());

        Sprite bulletSprite = new Sprite(bulletObject,
                bulletObject.getX(),
                bulletObject.getY(),
                bulletWidth,
                bulletHeight,
                Color.BLACK);

        addObject(bulletObject);
    }
}
