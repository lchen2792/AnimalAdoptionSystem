package com.animal.userservice.service;

import com.animal.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    @Value("${spring.security.token.secret-key}")
    private String secretKey;

    public Optional<Claims> resolveToken(String token) {
        return Optional
                .ofNullable(token)
                .filter(t -> t.startsWith(Constants.JWT_PREFIX))
                .map(t -> t.substring(7))
                .map(t -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(t).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()));
    }
}

