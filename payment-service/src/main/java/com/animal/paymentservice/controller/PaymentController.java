package com.animal.paymentservice.controller;


import com.animal.paymentservice.controller.model.ValidatePaymentMethodRequest;
import com.animal.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    @Autowired
    private transient PaymentService paymentService;

    @PostMapping("/validation")
    public Mono<ResponseEntity<String>> validate(@RequestBody @Validated Mono<ValidatePaymentMethodRequest> paymentDetail) {
        return paymentDetail
                .flatMap(paymentService::validate)
                .switchIfEmpty(Mono.error(new ValidationException("invalid payment method")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err -> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(WebExchangeBindException.class, err -> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }
}
