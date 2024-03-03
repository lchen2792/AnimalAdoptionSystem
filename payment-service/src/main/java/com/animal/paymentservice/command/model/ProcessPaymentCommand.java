package com.animal.paymentservice.command.model;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class ProcessPaymentCommand implements Serializable {
    @TargetAggregateIdentifier
    private final String paymentId;
    private final String applicationId;
    private final String userProfileId;
    private final String customerId;
    private final String paymentIntentId;
}
