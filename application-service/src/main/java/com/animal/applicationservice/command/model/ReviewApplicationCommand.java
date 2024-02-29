package com.animal.applicationservice.command.model;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class ReviewApplicationCommand implements Serializable {
    @TargetAggregateIdentifier
    private String applicationId;
}