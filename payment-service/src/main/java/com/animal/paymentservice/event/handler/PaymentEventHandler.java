package com.animal.paymentservice.event.handler;

import com.animal.common.event.PaymentProcessedEvent;
import com.animal.common.event.PaymentReversedEvent;
import com.animal.paymentservice.data.model.Payment;
import com.animal.paymentservice.data.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PaymentEventHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @EventHandler
    public void on(PaymentProcessedEvent event){
        Payment payment = Payment.builder()
                .applicationId(event.getApplicationId())
                .paymentId(event.getPaymentId())
                .customerId(event.getCustomerId())
                .paymentIntentId(event.getPaymentIntentId())
                .paymentStatus(event.getPaymentStatus())
                .userProfileId(event.getUserProfileId())
                .build();
        paymentRepository.save(payment).subscribe();
    }

    @EventHandler
    public void on(PaymentReversedEvent event) {
        paymentRepository
                .findById(event.getPaymentId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException(event.getPaymentId())))
                .flatMap(payment -> {
                    payment.setPaymentStatus(event.getPaymentStatus());
                    return paymentRepository.save(payment);
                })
                .subscribe();
    }
}
