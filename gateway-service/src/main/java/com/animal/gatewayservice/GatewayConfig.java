package com.animal.gatewayservice;

import com.animal.gatewayservice.filter.RoleBasedAuthGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder,
            RoleBasedAuthGatewayFilterFactory roleBasedAuthGatewayFilterFactory) {
        return builder.routes()
                .route("user-service",
                        r -> r
                                .path("/user-service/**")
                                .filters(f -> f
                                        .rewritePath("/(?<prefix>.+)-service/(?<remaining>.*)", "/${remaining}")
                                        .filter(roleBasedAuthGatewayFilterFactory
                                                .apply(new RoleBasedAuthGatewayFilterFactory
                                                        .Config(List.of("CUSTOMER", "EMPLOYEE", "ADMIN")))))
                                .uri("lb://user-service"))
                .route("application-service",
                        r -> r
                                .path("/application-service/**")
                                .filters(f -> f
                                        .rewritePath("/(?<prefix>.+)-service/(?<remaining>.*)", "/${remaining}")
                                        .filter(roleBasedAuthGatewayFilterFactory
                                        .apply(new RoleBasedAuthGatewayFilterFactory
                                                .Config(List.of("CUSTOMER", "EMPLOYEE", "ADMIN")))))
                                .uri("lb://application-service"))
                .build();
    }
}
