package com.akson.invaders.client.util;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * JavaFX data model for Score entities. Required for TableView in Highscore screen.
 */
public class HighScoreModel {

    private SimpleStringProperty username;
    private SimpleDoubleProperty score;

    public HighScoreModel(String username, Double score) {
        this.username = new SimpleStringProperty(username);
        this.score = new SimpleDoubleProperty(score);
    }


    public double getScore() {
        return score.get();
    }

    public void setScore(double score) {
        this.score = new SimpleDoubleProperty(score);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }

}