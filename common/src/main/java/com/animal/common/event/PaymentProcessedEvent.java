package com.animal.common.event;

import com.animal.common.status.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProcessedEvent implements Serializable {
    private String paymentId;
    private String applicationId;
    private String userProfileId;
    private String customerId;
    private String paymentIntentId;
    private PaymentStatus paymentStatus;
}
