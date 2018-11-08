package com.akson.invaders.server.repository;

import com.akson.invaders.server.entity.HighScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HighScoreRepository extends JpaRepository<HighScore, Long> {
}

