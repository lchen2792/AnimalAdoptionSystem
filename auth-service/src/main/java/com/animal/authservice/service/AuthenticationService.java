package com.animal.authservice.service;

import com.animal.authservice.dao.UserRepository;
import com.animal.authservice.dto.LoginRequest;
import com.animal.authservice.dto.RegistrationRequest;
import com.animal.authservice.exception.DuplicateUserIdentifierException;
import com.animal.authservice.model.Role;
import com.animal.authservice.model.User;
import com.animal.authservice.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public String registerUser(RegistrationRequest request) {
        Optional<User> userOptional = userRepository
                .findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            User user = User
                    .builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            UserRole userRole = UserRole
                    .builder()
                    .role(Role.CUSTOMER)
                    .user(user)
                    .active(true)
                    .build();
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            return jwtService.generateToken(user);
        } else {
            throw new DuplicateUserIdentifierException();
        }
    }

    public String authenticate(LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        return jwtService.generateToken((UserDetails) authentication.getPrincipal());
    }

}
