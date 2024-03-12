package com.animal.animalservice.command.model;

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
public class DeleteAnimalMediaCommand implements Serializable {
    @TargetAggregateIdentifier
    private String animalProfileId;
    private String mediaId;
}
