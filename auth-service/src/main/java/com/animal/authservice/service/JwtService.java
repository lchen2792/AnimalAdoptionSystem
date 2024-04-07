package com.animal.authservice.service;

import com.animal.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtService {
    @Value("${spring.security.token.life-time}")
    private Long tokenLifeTime;
    @Value("${spring.security.token.secret-key}")
    private String secretKey;

    public String generateToken(UserDetails user) {
        long timestamp = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.SPRING_SECURITY_SUBJECT, user.getUsername());
        claims.put(Constants.SPRING_SECURITY_AUTHORITIES, user.getAuthorities());

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
        log.info("processing request uri " + request.getRequestURI());

        return Optional
                .ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(t -> t.startsWith(Constants.JWT_PREFIX))
                .map(t -> t.substring(7))
                .map(token -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()));
    }
}

