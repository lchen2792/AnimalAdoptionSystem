package com.animal.applicationservice.exception;

public class ApplicationLimitException extends RuntimeException {
    public ApplicationLimitException() {
        super("too many applications for current user");
    }
}
