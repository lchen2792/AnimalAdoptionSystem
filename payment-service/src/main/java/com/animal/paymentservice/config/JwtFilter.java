package com.animal.paymentservice.config;


import com.animal.common.constant.Constants;
import com.animal.paymentservice.service.JwtService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter implements WebFilter {
    @Autowired
    private JwtService jwtService;

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.info("jwt filter runs for request id {}", exchange.getRequest().getId());
        return jwtService
                .resolveToken(Mono.just(exchange.getRequest()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Jwt token authentication failed")))
                .flatMap(claims -> {
                    Set<SimpleGrantedAuthority> authorities = ((List<LinkedHashMap<String, String>>) claims
                            .get(Constants.SPRING_SECURITY_AUTHORITIES))
                            .stream()
                            .map(a -> a.get("authority"))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            claims.get(Constants.SPRING_SECURITY_SUBJECT).toString(),
                            null,
                            authorities
                    );
                    return chain
                            .filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
                })
                .onErrorResume(err -> {
                    log.error(err.getClass() + " " + err.getMessage());
                    return chain.filter(exchange);
                });
    }
}
