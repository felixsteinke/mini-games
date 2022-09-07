package com.spring.server.game.exception;

public class TurnNotPossibleException extends Exception {
    public TurnNotPossibleException(String reason) {
        super("Reason: " + reason);
    }
}
