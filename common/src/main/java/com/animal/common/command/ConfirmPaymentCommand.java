package com.animal.common.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmPaymentCommand implements Serializable {
    @TargetAggregateIdentifier
    private String paymentId;
    private String applicationId;
    private String userProfileId;
    private String customerId;
    private String paymentIntentId;
}
