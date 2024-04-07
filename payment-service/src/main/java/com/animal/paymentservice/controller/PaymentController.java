package com.animal.paymentservice.controller;


import com.animal.paymentservice.controller.model.ValidatePaymentMethodRequest;
import com.animal.paymentservice.service.PaymentService;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class PaymentController {
    @Autowired
    private transient PaymentService paymentService;
    @Value("${stripe.application-fee}")
    private Long applicationFee;

    @PostMapping("/validate-payment-method")
    public Mono<ResponseEntity<String>> validate(@RequestBody @Validated Mono<ValidatePaymentMethodRequest> paymentDetail) {
        return paymentDetail
                .flatMap(paymentService::validatePaymentMethod)
                .switchIfEmpty(Mono.error(new ValidationException("invalid payment method")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err -> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("payment method validation failed"));
                })
                .onErrorResume(WebExchangeBindException.class, err -> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("payment info has error(s)"));
                });
    }

    @PostMapping("/process-payment/customer/{customerId}")
    public Mono<ResponseEntity<String>> processPayment(@PathVariable String customerId){
        return paymentService
                .processPayment(customerId, applicationFee)
                .switchIfEmpty(Mono.error(new ValidationException("invalid payment")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err-> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PutMapping("/confirm-payment/{paymentIntentId}")
    public Mono<ResponseEntity<Boolean>> confirmPayment(@PathVariable String paymentIntentId){
        return paymentService
                .confirmPayment(paymentIntentId)
                .switchIfEmpty(Mono.error(new ValidationException("failed to confirm payment")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err-> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @PutMapping("/cancel-payment/{paymentIntentId}")
    public Mono<ResponseEntity<Boolean>> cancelPayment(@PathVariable String paymentIntentId){
        return paymentService
                .revertPayment(paymentIntentId)
                .switchIfEmpty(Mono.error(new ValidationException("failed to cancel payment")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err-> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @DeleteMapping("/remove-payment-method/customer/{customerId}")
    public Mono<ResponseEntity<String>> remove(@PathVariable String customerId) {
        return paymentService
                .removePaymentMethod(customerId)
                .switchIfEmpty(Mono.error(new ValidationException("failed to remove payment method")))
                .map(ResponseEntity::ok)
                .onErrorResume(ValidationException.class, err -> {
                    log.error(err.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }
}
