package com.akson.invaders.client;

import java.util.UUID;

public class GameObject {

    private String id;
    private Sprite sprite;
    private GameObjectType type;
    private boolean dead;
    private int x;
    private int y;

    public GameObject() {
        this.id = UUID.randomUUID().toString();
    }

    public GameObject(GameObjectType type, int x, int y) {
        this.type = type;
        this.id = UUID.randomUUID().toString();
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public GameObjectType getType() {
        return type;
    }

    public void setType(GameObjectType type) {
        this.type = type;
    }
}
