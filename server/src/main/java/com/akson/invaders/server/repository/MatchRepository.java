package com.akson.invaders.server.repository;

import com.akson.invaders.server.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
