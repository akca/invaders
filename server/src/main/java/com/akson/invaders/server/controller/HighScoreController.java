package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.HighScore;
import com.akson.invaders.server.entity.User;
import com.akson.invaders.server.repository.HighScoreRepository;
import com.akson.invaders.server.util.ForbiddenException;
import com.akson.invaders.server.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
import java.util.List;


@RestController
public class HighScoreController {
    private HighScoreRepository highScoreRepository;

    @Autowired
    public HighScoreController(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }

    @GetMapping(value = "/highscores")
    public List<HighScore> getHighestScores() {
        return highScoreRepository.findTop3ByOrderByScoreDesc();
    }
}
