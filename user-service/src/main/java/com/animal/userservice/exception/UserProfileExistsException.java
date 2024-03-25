package com.animal.userservice.exception;

public class UserProfileExistsException extends RuntimeException {

    public UserProfileExistsException() {
        super("user profile exists");
    }
}
