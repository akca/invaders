package com.akson.invaders.client;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import java.util.Iterator;

public class StateManagerSP extends StateManager {

    @Override
    public void addObject(GameObject object) {
        gameObjects.put(object.getId(), object);

        addObjectToUI(object);
    }

    @Override
    public void moveHorizontal(String objectId, int amount) {
        GameObject gameObject = gameObjects.get(objectId);

        int newX = gameObject.getX() + amount;
        gameObject.setX(newX);

        updateObject(gameObject);
    }

    @Override
    public void moveVertical(String objectId, int amount) {
        GameObject gameObject = gameObjects.get(objectId);

        int newY = gameObject.getY() + amount;
        gameObject.setY(newY);

        updateObject(gameObject);
    }

    @Override
    public void run() {

        running.set(true);

        while (running.get()) {

            gameObjects.values().forEach(gameObject -> {
                switch (gameObject.getType()) {

                    case ENEMY_BULLET:
                        moveVertical(gameObject.getId(), 5);

                        Bounds bulletBounds = gameObject.getSprite().getBoundsInParent();
                        Bounds playerBounds = playerObject.getSprite().getBoundsInParent();

                        if (bulletBounds.intersects(playerBounds)) {

                            // decrement health by 20
                            playerObject.setHealth(playerObject.getHealth() - 20);

                            if (playerObject.getHealth() <= 0) {
                                playerObject.setDead(true);
                            }

                            gameObject.setDead(true);
                        }
                        break;

                    case BULLET:
                        moveVertical(gameObject.getId(), -5);

                        gameObjects.values().stream()
                                .filter(e -> e.getType().equals(GameObjectType.ENEMY))
                                .forEach(enemy -> {
                                    if (gameObject.getSprite().getBoundsInParent()
                                            .intersects(enemy.getSprite().getBoundsInParent())) {

                                        // decrement health by 20
                                        enemy.setHealth(enemy.getHealth() - 20);

                                        if (enemy.getHealth() <= 0) {
                                            enemy.setDead(true);
                                        }

                                        gameObject.setDead(true);
                                    }
                                });

                        break;

                    case ENEMY:

                        if (Math.random() < 0.01) {
                            shoot(gameObject);
                        }

                        break;
                }
            });

            /* remove dead or out of the screen objects */
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
                logger.debug("Sleep interrupted!");
            }
        }
    }

    @Override
    public void shoot(GameObject who) {

        GameObjectType bulletType;

        if (who.getType().equals(GameObjectType.ENEMY)) {
            bulletType = GameObjectType.ENEMY_BULLET;
        } else {
            bulletType = GameObjectType.BULLET;
        }

        GameObject bulletObject = new GameObject(bulletType, who.getX() + 15, who.getY());

        Sprite bulletSprite = new Sprite(bulletObject,
                bulletObject.getX(),
                bulletObject.getY(),
                5,
                20,
                Color.BLACK);

        addObject(bulletObject);
    }
}
