package com.akson.invaders.server.repository;

import com.akson.invaders.server.entity.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {

    HighScore findTopByOrderByScoreDesc();
}

