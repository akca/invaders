package com.akson.invaders.client;

import java.util.concurrent.ConcurrentHashMap;

public abstract class StateManager extends Thread {

    protected SpaceInvadersApp game;

    protected ConcurrentHashMap<String, GameObject> gameObjects;

    public StateManager(SpaceInvadersApp game) {
        this.game = game;
        this.gameObjects = new ConcurrentHashMap<>();
    }

    public abstract void addObject(GameObject object);

    public abstract void moveHorizontal(String objectId, int amount);

    public abstract void moveVertical(String objectId, int amount);

    public abstract void shoot(GameObject who);
}
