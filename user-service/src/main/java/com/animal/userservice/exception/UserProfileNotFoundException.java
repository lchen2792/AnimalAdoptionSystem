package com.animal.userservice.exception;

public class UserProfileNotFoundException extends RuntimeException{
    public UserProfileNotFoundException(String message) {
        super(message);
    }
}
