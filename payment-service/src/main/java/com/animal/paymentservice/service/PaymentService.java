package com.animal.paymentservice.service;

import com.animal.paymentservice.controller.model.ValidatePaymentMethodRequest;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentMethodCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@Slf4j
public class PaymentService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public Mono<String> validate(ValidatePaymentMethodRequest paymentDetail) {
        PaymentMethodCreateParams paymentMethodCreateParams =
                PaymentMethodCreateParams.builder()
                        .setType(PaymentMethodCreateParams.Type.CARD)
                        .setCard(
                                PaymentMethodCreateParams.CardDetails.builder()
                                        .setNumber(paymentDetail.getCardNumber())
                                        .setExpMonth(paymentDetail.getValidThruMonth().longValue())
                                        .setExpYear(paymentDetail.getValidThruYear().longValue())
                                        .setCvc(paymentDetail.getCVV().toString())
                                        .build()
                        )
                        .build();

        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodCreateParams);
            CustomerCreateParams customerCreateParams =
                    CustomerCreateParams.builder()
                            .setName(paymentDetail.getCardholderName())
                            .setPaymentMethod(paymentMethod.getId())
                            .build();
            return Mono.just(Customer.create(customerCreateParams).getId());
        } catch (StripeException e) {
            log.error(e.getMessage());
            return Mono.empty();
        }
    }
}
