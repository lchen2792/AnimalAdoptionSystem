package com.animal.authservice.exception;

public class DuplicateUserIdentifierException extends RuntimeException {
    public DuplicateUserIdentifierException() {
        super("username existed");
    }
}

