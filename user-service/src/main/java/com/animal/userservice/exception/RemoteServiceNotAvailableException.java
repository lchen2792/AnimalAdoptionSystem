package com.animal.userservice.exception;

public class RemoteServiceNotAvailableException extends RuntimeException {

    public RemoteServiceNotAvailableException() {
        super("remote service not available");
    }
}
