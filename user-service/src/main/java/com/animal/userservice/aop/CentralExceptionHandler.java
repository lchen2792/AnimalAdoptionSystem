package com.animal.userservice.aop;

import com.animal.userservice.exception.InvalidPaymentDetailException;
import com.animal.userservice.exception.MatchingException;
import com.animal.userservice.exception.RemoteServiceNotAvailableException;
import com.animal.userservice.exception.UserProfileNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler(value = {UserProfileNotFoundException.class, InvalidPaymentDetailException.class, MatchingException.class})
    public ResponseEntity<String> handleUser(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {RemoteServiceNotAvailableException.class})
    public ResponseEntity<String> handleRemoteService(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}

