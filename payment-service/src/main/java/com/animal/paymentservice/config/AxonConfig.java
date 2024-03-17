package com.animal.paymentservice.config;

import com.animal.paymentservice.interceptor.CommandDispatchInterceptor;
import com.animal.paymentservice.interceptor.CommandHandlerInterceptor;
import com.animal.paymentservice.interceptor.QueryDispatchInterceptor;
import com.animal.paymentservice.interceptor.QueryHandlerInterceptor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
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

    @Bean
    public CommandBus registerCommandInterceptors(CommandDispatchInterceptor dispatchInterceptor, CommandHandlerInterceptor handlerInterceptor) {
        CommandBus commandBus = SimpleCommandBus.builder().build();
        commandBus.registerDispatchInterceptor(dispatchInterceptor);
        commandBus.registerHandlerInterceptor(handlerInterceptor);
        return commandBus;
    }
}
