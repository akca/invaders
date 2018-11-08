package com.akson.invaders.server.entity;

import com.akson.invaders.server.entity.attr.MatchState;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Multiplayer Match entity for keeping game state, player list and more.
 */
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

    @OneToMany(mappedBy = "match")
    private List<Player> players;

    /* do not persist serverPort since it's unnecessary */
    @Transient
    private int serverPort;

    public Match(MatchState state, int serverPort) {
        this.players = new ArrayList<>();
        this.serverPort = serverPort;
        this.state = state;
        this.date = OffsetDateTime.now();
    }

    /**
     * Searches given username in the players list of the match.
     *
     * @param username username to find
     * @return User found or null (if not found).
     */
    public User findUser(String username) {

        for (Player p : players) {
            if (p.getUser().getUsername().equals(username)) {
                return p.getUser();
            }
        }

        return null;
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

}
