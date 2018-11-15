package com.akson.invaders.server.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Single player game score entity for creating leader boards.
 */
@Entity
@Table(name = "SCORE")
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCORE_ID")
    private long id;

    @Column(name = "DATE")
    private OffsetDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SCORE")
    private Long score;

    public Score() {
    }

    //TODO: consider default values of params
    public Score(Long score) {
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", user='" + user + '\'' +
                ", score=" + score +
                '}';
    }
}
