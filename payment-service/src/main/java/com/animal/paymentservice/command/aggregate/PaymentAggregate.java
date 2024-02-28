package com.animal.paymentservice.command.aggregate;

import com.animal.paymentservice.command.model.ProcessPaymentCommand;
import com.animal.paymentservice.command.model.ReversePaymentCommand;
import com.animal.paymentservice.data.model.PaymentDetail;
import com.animal.paymentservice.data.model.PaymentStatus;
import com.animal.paymentservice.event.model.PaymentProcessedEvent;
import com.animal.paymentservice.event.model.PaymentReversedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {
    @AggregateIdentifier
    private String paymentId;
    private String applicationId;
    private String userProfileId;
    private PaymentDetail paymentDetail;
    private PaymentStatus paymentStatus;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command){
        PaymentProcessedEvent event = PaymentProcessedEvent
                .builder()
                .paymentId(command.getPaymentId())
                .applicationId(command.getApplicationId())
                .userProfileId(command.getUserProfileId())
                .paymentStatus(PaymentStatus.PROCESSED)
                .build();
        //todo integrate stripe api
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event){
        this.paymentId = event.getPaymentId();
        this.applicationId = event.getApplicationId();
        this.userProfileId = event.getUserProfileId();
        this.paymentStatus = event.getPaymentStatus();
    }

    @CommandHandler
    public void on(ReversePaymentCommand command) {
        PaymentReversedEvent event = PaymentReversedEvent
                .builder()
                .paymentId(command.getPaymentId())
                .applicationId(command.getApplicationId())
                .userProfileId(command.getUserProfileId())
                .paymentStatus(PaymentStatus.REVERSED)
                .message(command.getMessage())
                .build();
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(PaymentReversedEvent event) {
        this.paymentStatus = event.getPaymentStatus();
    }
}
