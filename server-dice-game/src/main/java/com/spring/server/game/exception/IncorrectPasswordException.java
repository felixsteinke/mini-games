package com.spring.server.game.exception;

public class IncorrectPasswordException extends Exception {
    public IncorrectPasswordException(String password) {
        super("Incorrect password: " + password);
    }
}
