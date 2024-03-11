package com.animal.paymentservice.command.aggregate;

import com.animal.common.command.ProcessPaymentCommand;
import com.animal.common.command.ReversePaymentCommand;
import com.animal.common.event.PaymentProcessedEvent;
import com.animal.common.event.PaymentReversedEvent;
import com.animal.common.status.PaymentStatus;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

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
        log.info("process payment {}", command.getPaymentId());
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
        log.info("process payment command processed");
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
        log.info("reverse payment {}", command.getPaymentId());
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
        log.info("reverse payment command processed");
    }

    @EventSourcingHandler
    public void on(PaymentReversedEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
