package com.animal.applicationservice.command.model;

import com.animal.applicationservice.data.model.PaymentDetail;
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
    private final PaymentDetail paymentDetail;
}
