package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Experience {
    private Level withAdoptingSpecies;
    private Level withAdoptingBreed;
    private Level withAnimalAdoption;
}
