package com.animal.userservice.service;

import com.animal.common.constant.Constants;
import com.animal.userservice.controller.model.ValidatePaymentMethodRequest;
import com.animal.userservice.exception.RemoteServiceNotAvailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentProcessingService {
    private static final String PAYMENT_SERVICE = "payment-service";
    @Autowired
    public WebClient webClient;
    @Value("${payment.validation.base-url}")
    private String baseUrl;

    @Async
    @Retry(name = PAYMENT_SERVICE)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "validatePaymentFallback")
    public CompletableFuture<String> validatePaymentMethod(
            ValidatePaymentMethodRequest validatePaymentMethodRequest) {
        return webClient
                .post()
                .uri(baseUrl + "/validate-payment-method")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromPublisher(Mono.just(validatePaymentMethodRequest), ValidatePaymentMethodRequest.class))
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> validatePaymentFallback(
            ValidatePaymentMethodRequest validatePaymentMethodRequest,
            Throwable ex) {
        return CompletableFuture.failedFuture(new RemoteServiceNotAvailableException());
    }

    @Retry(name = PAYMENT_SERVICE)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "deletePaymentMethodFallback")
    public CompletableFuture<String> deletePaymentMethod(
            String customerId) {
        return webClient
                .delete()
                .uri(baseUrl + "/remove-payment-method/customer/" + customerId)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture();
    }

    public CompletableFuture<String> deletePaymentMethodFallback(
            String customerId,
            Throwable ex){
        return CompletableFuture.failedFuture(new RemoteServiceNotAvailableException());
    }
}
