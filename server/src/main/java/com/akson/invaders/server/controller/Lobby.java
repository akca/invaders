package com.akson.invaders.server.controller;

import com.akson.invaders.common.entity.Match;
import com.akson.invaders.common.entity.Player;
import com.akson.invaders.common.entity.User;
import com.akson.invaders.common.entity.attr.MatchState;
import com.akson.invaders.server.MPGameManager;
import com.akson.invaders.server.repository.MatchRepository;
import com.akson.invaders.server.repository.PlayerRepository;
import com.akson.invaders.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;

/**
 * Gathers users that want to play a multi-player game.
 * Starts a new match between them when there are at least 2 of them.
 * <p>
 * TODO: Design of this class is not finalized. Logic is probably going to change radically.
 */
@RestController
public class Lobby {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;

    private Map<String, User> lobbyUsers = new LinkedHashMap<>();
    private List<Match> matches = new ArrayList<>();

    @Autowired
    public Lobby(MatchRepository matchRepository,
                 UserRepository userRepository,
                 PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Gathers users in the {@link Lobby#lobbyUsers}. When there are 2 users, start a match between them immediately.
     *
     * @param request HttpServletRequest object for fetching logged in user
     * @return Match object if we have enough users, null otherwise.
     */
    @GetMapping(value = "/enterlobby")
    public Match enterLobby(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String loginUsername = principal.getName();

        // if this user has a active match, return it
        for (Match m : matches) {
            if (m.findUser(loginUsername) != null) {
                return m;
            }
        }

        // fetch logged in user from db
        User loginUser = userRepository.findByUsername(loginUsername);

        if (!lobbyUsers.containsKey(loginUsername)) {
            lobbyUsers.put(loginUsername, loginUser);
        }

        // if at least 2 users are found on lobby, create a new match between them.
        if (lobbyUsers.size() >= 2) {

            Collection<User> users = lobbyUsers.values();

            User user2 = users.iterator().next();

            lobbyUsers.remove(loginUser.getUsername());
            lobbyUsers.remove(user2.getUsername());

            // start a new MPGameManager thread for this match
            MPGameManager server = new MPGameManager();
            server.start();

            Match match = new Match(MatchState.LOBBY, server.getPort());

            matchRepository.save(match);

            Player p1 = new Player(match, user2);
            Player p2 = new Player(match, loginUser);

            playerRepository.save(p1);
            playerRepository.save(p2);

            match.getPlayers().add(p1);
            match.getPlayers().add(p2);

            matchRepository.save(match);

            matches.add(match);

            return match;

        } else {

            return null;
        }
    }

    /**
     * Makes logged in user leave the lobby.
     *
     * @param request HttpServletRequest object for fetching logged in user
     */
    @GetMapping(value = "/leavelobby")
    public void leaveLobby(HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();
        String loginUsername = principal.getName();

        lobbyUsers.remove(loginUsername);
    }

    /**
     * Returns list of all waiting lobby users.
     *
     * @return List of all waiting lobby users
     */
    @GetMapping(value = "/lobby")
    public Collection<User> listLobby() {

        return lobbyUsers.values();
    }
}
