package com.animal.userservice.config;

import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {
    @Value("${animal.profile.base-url}")
    private String graphQlBaseUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.builder().clientConnector(clientConnector()).build();
    }

    @Bean
    public HttpGraphQlClient graphqlClient() {
        return HttpGraphQlClient.create(WebClient.builder().clientConnector(clientConnector()).baseUrl(graphQlBaseUrl).build());
    }

    private ReactorClientHttpConnector clientConnector(){
        HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
        return new ReactorClientHttpConnector(httpClient);
    }
}
