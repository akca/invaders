package com.akson.invaders.server.repository;

import com.akson.invaders.server.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Page<Score> findByOrderByScoreDesc(Pageable pageable);

    Page<Score> findByDateBetweenOrderByScoreDesc(OffsetDateTime start, OffsetDateTime end, Pageable pageable);
}

