package com.spring.server.game.exception;

public class DuplicateException extends Exception {
    public DuplicateException(String identifier) {
        super("'" + identifier + "' already exists.");
    }
}
