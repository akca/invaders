package com.akson.invaders.client;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;

import java.util.Iterator;

import static com.akson.invaders.client.SpaceInvadersApp.WINDOW_HEIGHT;

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

        while (true) {

            gameObjects.values().forEach(gameObject -> {
                switch (gameObject.getType()) {

                    case ENEMY_BULLET:
                        moveVertical(gameObject.getId(), 5);

                        Bounds bulletBounds = gameObject.getSprite().getBoundsInParent();
                        Bounds playerBounds = playerObject.getSprite().getBoundsInParent();

                        if (bulletBounds.intersects(playerBounds)) {
                            playerObject.setDead(true);
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
                                        enemy.setDead(true);
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

            Iterator<GameObject> it = gameObjects.values().iterator();

            while (it.hasNext()) {

                GameObject gameObject = it.next();

                if (gameObject.isDead()
                        || gameObject.getY() > WINDOW_HEIGHT
                        || gameObject.getY() < 0) {

//                    logger.debug(gameObject.toString());

                    deleteObject(gameObject);
                    it.remove();
                }
            }

            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
