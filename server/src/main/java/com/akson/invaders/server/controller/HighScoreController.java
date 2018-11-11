package com.akson.invaders.server.controller;

import com.akson.invaders.server.entity.HighScore;
import com.akson.invaders.server.repository.HighScoreRepository;
import com.akson.invaders.server.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST controller that is responsible for highscore data.
 */
@RestController
public class HighScoreController {

    private HighScoreRepository highScoreRepository;

    @Autowired
    public HighScoreController(HighScoreRepository highScoreRepository) {
        this.highScoreRepository = highScoreRepository;
    }

    /**
     * Adds HighScore objects that sent to the given URL using HTTP POST method.
     *
     * @param highScore HighScore object to be added
     * @return persisted HighScore object
     */
    @PostMapping(value = "/score")
    public HighScore addHighScore(@RequestBody HighScore highScore) {

        return highScoreRepository.save(highScore);
    }


    /**
     * Returns top highscore details.
     *
     * @return persisted HighScore object
     */
    @GetMapping(value = "/score")
    public HighScore getHighScoreDetails() {

        return highScoreRepository.findTopByOrderByScoreDesc();
    }

    /**
     * Returns score details for given score id.
     *
     * @param id Score id to fetch
     * @return persisted HighScore object
     */
    @GetMapping(value = "/score/{id}")
    public HighScore getScoreDetails(@PathVariable Long id) {

        final Optional<HighScore> optionalScore = highScoreRepository.findById(id);

        if (optionalScore.isPresent())
            return optionalScore.get();
        else {
            throw new NotFoundException("Cannot find a highscore with this id!");
        }
    }

}
