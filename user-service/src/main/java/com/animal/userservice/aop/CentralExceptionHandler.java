package com.animal.userservice.aop;

import com.animal.userservice.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CentralExceptionHandler {

    @ExceptionHandler(value = {UserProfileNotFoundException.class, UserProfileExistsException.class, InvalidPaymentDetailException.class, MatchingException.class})
    public ResponseEntity<String> handleUser(Exception e){
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {RemoteServiceNotAvailableException.class})
    public ResponseEntity<String> handleRemoteService(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}

