package com.animal.animalservice.event.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AnimalDeletedEvent implements Serializable {
    private String animalProfileId;
}
