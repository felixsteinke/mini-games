package com.spring.server.game.exception;

public class FullGameException extends Exception {
    public FullGameException(String game, int size) {
        super("The game '" + game + "' with " + size + "players is full.");
    }
}
