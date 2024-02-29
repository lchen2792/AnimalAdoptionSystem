package com.animal.animalservice.config;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(){
        return builder -> builder.scalar(ApolloScalars.Upload);
    }
}

