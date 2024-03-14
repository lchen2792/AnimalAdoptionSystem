package com.animal.authservice.controller;

import com.animal.authservice.dto.LoginRequest;
import com.animal.authservice.dto.RegistrationRequest;
import com.animal.authservice.exception.DuplicateUserIdentifierException;
import com.animal.authservice.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/registration/user")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request){
        try {
            return ResponseEntity.ok(authenticationService.registerUser(request));
        } catch (DuplicateUserIdentifierException e) {
            return ResponseEntity.badRequest().body("user identifier existed");
        }
    }

    @GetMapping("/auth")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequest request) {
        try {
            String authenticate = authenticationService.authenticate(request);
            return ResponseEntity.ok(authenticate);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("authentication failed");
        }
    }
}
