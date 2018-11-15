package com.akson.invaders.server.repository;

import com.akson.invaders.server.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Score findTopByOrderByScoreDesc();
}

