package com.animal.paymentservice.service;

import com.animal.common.constant.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Mono<Claims> resolveToken(Mono<ServerHttpRequest> request) {
        log.info("jwt service resolves token for request id {}", request.map(ServerHttpRequest::getId));
        return request
                .map(ServerHttpRequest::getHeaders)
                .filter(headers -> headers.containsKey(HttpHeaders.AUTHORIZATION))
                .map(headers -> headers.getFirst(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.startsWith(Constants.JWT_PREFIX))
                .map(t -> t.substring(7))
                .map(token -> Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody())
                .filter(claims -> claims.getExpiration().after(new Date()))
                .log();
    }
}

