package com.animal.gatewayservice.filter;

import com.animal.gatewayservice.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Order(-1)
public class RoleBasedAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleBasedAuthGatewayFilterFactory.Config> {

    @Autowired
    private JwtService jwtService;

    public RoleBasedAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            System.out.println("applying filter");
            return jwtService
                    .authenticate(request)
                    .filter(claims -> jwtService.authorize(claims, config.getRoles()))
                    .map(claims -> {
                        exchange.getRequest().mutate().header("Auth-Email", String.valueOf(claims.get("subject"))).build();
                        return chain.filter(exchange);
                    })
                    .orElseGet(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return response.setComplete();
                    });
        }, -1);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        private List<String> roles;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("roles");
    }
}
