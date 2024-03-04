package com.animal.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${animal.profile.base-url}")
    private String graphQlBaseUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.create();
    }

    @Bean
    public HttpGraphQlClient graphqlClient() {
        return HttpGraphQlClient.create(WebClient.builder().baseUrl(graphQlBaseUrl).build());
    }
}
