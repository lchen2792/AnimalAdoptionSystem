package com.animal.userservice.controller.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@Builder
public class AnimalProfileForMatch {
    private String animalProfileId;
    private AnimalStatus status;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
}
