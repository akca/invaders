package com.akson.invaders.server.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A User and their score constitute a Player entity. Every match has more than one Players.
 */
@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATCH_ID")
    private Match match;

    @Id
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SCORE")
    private Long score;

    public Player(Match match, User user) {
        this.match = match;
        this.user = user;
        this.score = 0L;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
