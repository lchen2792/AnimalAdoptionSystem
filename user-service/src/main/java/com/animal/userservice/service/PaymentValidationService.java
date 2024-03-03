package com.animal.userservice.service;

import com.animal.userservice.controller.model.ValidatePaymentMethodRequest;
import com.animal.userservice.exception.RemoteServiceNotAvailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentValidationService {
    private static final String PAYMENT_SERVICE = "payment-service";
    @Autowired
    public WebClient webClient;

    @Async
    @Retry(name = PAYMENT_SERVICE)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "validatePaymentFallback")
    public CompletableFuture<String> validatePayment(ValidatePaymentMethodRequest validatePaymentMethodRequest) {
        return webClient
                .post()
                .uri("/validate-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(validatePaymentMethodRequest), ValidatePaymentMethodRequest.class))
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> validatePaymentFallback(ValidatePaymentMethodRequest validatePaymentMethodRequest, Throwable ex) {
        return CompletableFuture.failedFuture(new RemoteServiceNotAvailableException());
    }
}