package com.akson.invaders.common.entity;

import com.akson.invaders.common.entity.attr.MatchState;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "STATE", nullable = false)
    private MatchState state;

    @Column(name = "DATE", nullable = false)
    private OffsetDateTime date;

    @OneToMany(mappedBy = "match")
    private List<Player> players;

    /* do not persist serverPort since it's unnecessary */
    @Transient
    private int serverPort;

    public Match() {
    }

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

            User user = p.getUser();

            if (user.getUsername().equals(username)) {
                return user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return id == match.id &&
                serverPort == match.serverPort &&
                state == match.state &&
                Objects.equals(date, match.date) &&
                match.players.equals(players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, date, players, serverPort);
    }
}
