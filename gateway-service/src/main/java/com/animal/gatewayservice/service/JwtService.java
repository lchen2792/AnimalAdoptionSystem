package com.animal.gatewayservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtService {

    @Value("${spring.security.token.secret-key}")
    private String secretKey;

    /**
     * extract, parse and validate token
     *
     * @param request http request
     * @return token claims if extracted, parsed and validated
     */
    public Optional<Claims> authenticate(ServerHttpRequest request) {
        log.info("authenticating request");
        return Optional.ofNullable(request.getHeaders().get(HttpHeaders.AUTHORIZATION))
                .map(auths -> auths.get(0))
                .filter(t -> t.startsWith("Bearer "))
                .map(t -> t.substring(7))
                .map(token -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()));
    }

    public Boolean authorize(Claims credentials, List<String> roles){
        log.info("authorizing request");
        return ((List<LinkedHashMap<String, String>>) credentials.get("authorities"))
                .stream()
                .map(a -> a.get("authority"))
                .anyMatch(roles::contains);
    }
}
