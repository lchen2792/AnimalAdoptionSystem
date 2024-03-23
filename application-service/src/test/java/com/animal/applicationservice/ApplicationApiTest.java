package com.animal.applicationservice;

import com.animal.applicationservice.data.model.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Objects;


@SpringBootTest(classes = ApplicationServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationApiTest {
    @LocalServerPort
    private int port;

    @Test
    @DirtiesContext
    public void testFindApplications(){
        WebClient webClient = WebClient.builder().clientConnector(httpConnector()).build();

        StepVerifier.create(webClient
                .get()
                .uri("http://localhost:" + port + "/applications?page=0&size=1")
                .retrieve()
                .bodyToFlux(Application.class))
                .verifyComplete();
    }

    private static ReactorClientHttpConnector httpConnector() {
        HttpClient httpClient = HttpClient.create().wiretap(true);
        return new ReactorClientHttpConnector(httpClient);
    }
}
