package com.animal.animalservice.event.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimalDeletedEvent {
    private String animalProfileId;
}
