package com.animal.common.command;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class ReleaseAnimalCommand implements Serializable {
    @TargetAggregateIdentifier
    private String animalProfileId;
    private String applicationId;
    private String userProfileId;
    private String message;
}
