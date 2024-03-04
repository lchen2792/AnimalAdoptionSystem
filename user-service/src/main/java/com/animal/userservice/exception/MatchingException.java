package com.animal.userservice.exception;

public class MatchingException extends RuntimeException {
    public MatchingException() {
        super("Failed to match");
    }
}
