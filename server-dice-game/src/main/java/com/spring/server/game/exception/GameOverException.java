package com.spring.server.game.exception;

import com.spring.server.game.cardOwner.Player;
import lombok.Getter;

public class GameOverException extends Exception {
    @Getter
    private final Player player;

    public GameOverException(Player player) {
        super("Player won the game: " + player.toString());
        this.player = player;
    }
}
