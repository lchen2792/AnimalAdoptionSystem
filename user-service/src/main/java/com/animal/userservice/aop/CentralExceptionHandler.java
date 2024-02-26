package com.animal.userservice.aop;

import com.animal.userservice.exception.UserProfileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler(value = {UserProfileNotFoundException.class})
    public ResponseEntity<String> handle(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
