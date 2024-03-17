package com.animal.userservice.service;

import com.animal.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {
    @Value("${spring.security.token.secret-key}")
    private String secretKey;

    /**
     * extract, parse and validate token
     *
     * @param request http request
     * @return token claims if extracted, parsed and validated
     */
    public Optional<Claims> resolveToken(HttpServletRequest request) {
        return Optional
                .ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(t -> t.startsWith(Constants.JWT_PREFIX))
                .map(t -> t.substring(7))
                .map(token -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()));
    }
}

