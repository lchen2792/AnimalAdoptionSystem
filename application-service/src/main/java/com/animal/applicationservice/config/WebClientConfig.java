package com.animal.applicationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${web-client.base-url}")
    private String baseUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.create(baseUrl);
    }
}
