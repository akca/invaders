package com.akson.invaders.server.repository;


import com.akson.invaders.common.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    Page<Score> findByOrderByScoreAsc(Pageable pageable);

    Page<Score> findByDateBetweenOrderByScoreAsc(OffsetDateTime start, OffsetDateTime end, Pageable pageable);
}

