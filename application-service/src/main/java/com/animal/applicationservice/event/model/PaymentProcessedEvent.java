package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.PaymentStatus;
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
    private PaymentStatus paymentStatus;
}
