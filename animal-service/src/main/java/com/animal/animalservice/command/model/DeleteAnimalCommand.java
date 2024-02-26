package com.animal.animalservice.command.model;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class DeleteAnimalCommand implements Serializable {
    @TargetAggregateIdentifier
    private String animalProfileId;
}
