package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.Score;
import com.akson.invaders.server.repository.ScoreRepository;
import com.akson.invaders.server.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller that is responsible for singleplayer score data.
 */
@RestController
public class ScoreController {

    private ScoreRepository scoreRepository;

    @Autowired
    public ScoreController(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * Adds Score objects that sent to the given URL using HTTP POST method.
     *
     * @param score Score object to be added
     * @return persisted Score object
     */
    @PostMapping(value = "/score")
    public Score addScore(@RequestBody Score score) {

        return scoreRepository.save(score);
    }


    /**
     * Returns top highscore details.
     *
     * @return persisted Score object
     */
    @GetMapping(value = "/score")
    public Score getHighScoreDetails() {

        return scoreRepository.findTopByOrderByScoreDesc();
    }

    /**
     * Returns score details for given score id.
     *
     * @param id Score id to fetch
     * @return persisted Score object
     */
    @GetMapping(value = "/score/{id}")
    public Score getScoreDetails(@PathVariable Long id) {

        final Optional<Score> optionalScore = scoreRepository.findById(id);

        if (optionalScore.isPresent())
            return optionalScore.get();
        else {
            throw new NotFoundException("Cannot find a score with this id!");
        }
    }

}
