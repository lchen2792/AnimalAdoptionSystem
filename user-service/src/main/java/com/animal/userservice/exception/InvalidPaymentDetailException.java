package com.animal.userservice.exception;

public class InvalidPaymentDetailException extends RuntimeException {
    public InvalidPaymentDetailException() {
        super("invalid payment info");
    }
}
