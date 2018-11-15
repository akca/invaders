package com.akson.invaders.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A User and their score constitute a Player entity. Every match has more than one Players.
 */
@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAYER_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "MATCH_ID")
    @JsonIgnore
    private Match match;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SCORE")
    private Long score;

    @JsonIgnore
    @Transient
    private String ip;

    public Player() {
    }

    public Player(Match match, User user) {
        this.match = match;
        this.user = user;
        this.score = 0L;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                Objects.equals(match, player.match) &&
                Objects.equals(user, player.user) &&
                Objects.equals(score, player.score) &&
                Objects.equals(ip, player.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, match, user, score, ip);
    }
}
