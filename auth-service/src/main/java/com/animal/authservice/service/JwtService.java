package com.animal.authservice.service;

import com.animal.authservice.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtService {
    @Value("${spring.security.token.life-time}")
    private Long tokenLifeTime;
    @Value("${spring.security.token.secret-key}")
    private String secretKey;

    public String generateToken(UserDetails user) {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("subject", user.getUsername());
        claims.put("authorities", user.getAuthorities());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(timestamp))
                .setExpiration(new Date(timestamp + tokenLifeTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * extract, parse and validate token
     *
     * @param request http request
     * @return token claims if extracted, parsed and validated
     */
    public Optional<Claims> resolveToken(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader("Authorization"))
                .filter(t -> t.startsWith("Bearer "))
                .map(t -> t.substring(7))
                .map(token -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()));
    }
}

