package com.akson.invaders.server.entity;

import com.akson.invaders.server.entity.attr.MatchState;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "MATCHES")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MATCH_ID")
    private long id;

    @Column(name = "STATE")
    private MatchState state;

    @Column(name = "DATE")
    private OffsetDateTime date;

    @ManyToOne
    @JoinColumn(name = "USER1_ID")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "USER2_ID")
    private User user2;

    @Column(name = "SCORE1")
    private long score1;

    @Column(name = "SCORE2")
    private long score2;

    @Transient
    private int serverPort;

    public Match(User user1, User user2, int serverPort, MatchState state) {
        this.user1 = user1;
        this.user2 = user2;
        this.serverPort = serverPort;
        this.state = state;
        this.score1 = 0;
        this.score2 = 0;
        this.date = OffsetDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public long getScore1() {
        return score1;
    }

    public void setScore1(long score1) {
        this.score1 = score1;
    }

    public long getScore2() {
        return score2;
    }

    public void setScore2(long score2) {
        this.score2 = score2;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

}
