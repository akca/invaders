package com.akson.invaders.server.controller;

import com.akson.invaders.common.entity.Score;
import com.akson.invaders.server.repository.ScoreRepository;
import com.akson.invaders.server.util.ControllerException;
import com.akson.invaders.server.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
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

        try {
            return scoreRepository.save(score);
        } catch (Exception e) {
            throw new ControllerException("Score addition failed!");
        }
    }


    /**
     * Returns highscore entries, all time.
     * <p>
     * This returns page 2 (0-indexed) of scores with page size 10.
     * /score/all?size=10&page=2
     *
     * @return a page with scores.
     */
    @GetMapping(value = "/score/all")
    public Page<Score> getHighScoreDetailsAllTime(Pageable pageable) {

        return scoreRepository.findByOrderByScoreDesc(pageable);
    }

    /**
     * Returns monthly highscore entries.
     * <p>
     * This returns page 2 (0-indexed) of monthly scores with page size 10.
     * /score/month?size=10&page=2
     *
     * @return a page with scores.
     */
    @GetMapping(value = "/score/month")
    public Page<Score> getHighScoreDetailsMonthly(Pageable pageable) {

        return scoreRepository.findByDateBetweenOrderByScoreDesc(
                OffsetDateTime.now().minusMonths(1), OffsetDateTime.now(), pageable);
    }


    /**
     * Returns weekly highscore entries.
     * <p>
     * This returns page 2 (0-indexed) of weekly scores with page size 10.
     * /score/week?size=10&page=2
     *
     * @return a page with scores.
     */
    @GetMapping(value = "/score/week")
    public Page<Score> getHighScoreDetailsWeekly(Pageable pageable) {

        return scoreRepository.findByDateBetweenOrderByScoreDesc(
                OffsetDateTime.now().minusDays(7), OffsetDateTime.now(), pageable);
    }


    /**
     * Returns daily highscore entries.
     * <p>
     * This returns page 2 (0-indexed) of daily scores with page size 10.
     * /score/day?size=10&page=2
     *
     * @return a page with scores.
     */
    @GetMapping(value = "/score/day")
    public Page<Score> getHighScoreDetailsDaily(Pageable pageable) {

        return scoreRepository.findByDateBetweenOrderByScoreDesc(
                OffsetDateTime.now().minusDays(1), OffsetDateTime.now(), pageable);
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
