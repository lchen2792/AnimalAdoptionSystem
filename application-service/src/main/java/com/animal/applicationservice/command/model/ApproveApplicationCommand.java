package com.animal.applicationservice.command.model;

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
public class ApproveApplicationCommand implements Serializable {
    @TargetAggregateIdentifier
    private String applicationId;
    private String paymentId;
}
