package com.akson.invaders.server.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Single player game score entity for creating leader boards.
 */
@Entity
@Table(name = "HIGHSCORE")
public class HighScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCORE_ID")
    private long id;

    @Column(name = "DATE")
    private OffsetDateTime date;

    @ManyToOne
    @JoinColumn(name = "PLAYER_ID")
    private Player player;

    @Column(name = "SCORE")
    private Long score;
}
