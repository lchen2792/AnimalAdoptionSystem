package com.animal.paymentservice.command.aggregate;

import com.animal.paymentservice.command.model.ProcessPaymentCommand;
import com.animal.paymentservice.command.model.ReversePaymentCommand;
import com.animal.paymentservice.data.model.PaymentStatus;
import com.animal.paymentservice.event.model.PaymentProcessedEvent;
import com.animal.paymentservice.event.model.PaymentReversedEvent;
import com.animal.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.factory.annotation.Autowired;

@Aggregate
@NoArgsConstructor
@Slf4j
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String applicationId;
    private String userProfileId;
    private String customerId;
    private String paymentIntentId;
    private PaymentStatus paymentStatus;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command){
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent
                .builder()
                .paymentId(command.getPaymentId())
                .applicationId(command.getApplicationId())
                .userProfileId(command.getUserProfileId())
                .customerId(command.getCustomerId())
                .paymentIntentId(command.getPaymentIntentId())
                .paymentStatus(PaymentStatus.PROCESSED)
                .build();

        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event){
        this.paymentId = event.getPaymentId();
        this.applicationId = event.getApplicationId();
        this.userProfileId = event.getUserProfileId();
        this.paymentStatus = event.getPaymentStatus();
        this.customerId = event.getCustomerId();
        this.paymentIntentId = event.getPaymentIntentId();
    }

    @CommandHandler
    public void on(ReversePaymentCommand command) {
        PaymentReversedEvent paymentReversedEvent = PaymentReversedEvent
                .builder()
                .paymentId(command.getPaymentId())
                .applicationId(command.getApplicationId())
                .userProfileId(command.getUserProfileId())
                .customerId(customerId)
                .paymentIntentId(paymentIntentId)
                .paymentStatus(PaymentStatus.REVERSED)
                .message(command.getMessage())
                .build();

        AggregateLifecycle.apply(paymentReversedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentReversedEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
