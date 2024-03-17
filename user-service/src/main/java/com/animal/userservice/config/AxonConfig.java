package com.animal.userservice.config;

import com.animal.userservice.interceptor.QueryDispatchInterceptor;
import com.animal.userservice.interceptor.QueryHandlerInterceptor;
import org.axonframework.queryhandling.QueryBus;
import org.axonframework.queryhandling.SimpleQueryBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public QueryBus registerQueryInterceptors(QueryDispatchInterceptor dispatchInterceptor, QueryHandlerInterceptor handlerInterceptor) {
        QueryBus queryBus = SimpleQueryBus.builder().build();
        queryBus.registerDispatchInterceptor(dispatchInterceptor);
        queryBus.registerHandlerInterceptor(handlerInterceptor);
        return queryBus;
    }
}
