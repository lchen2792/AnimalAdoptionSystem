package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Knowledge {
    private Level ofAdoptingSpecies;
    private Level ofAdoptingBreed;
    private Level ofAnimalAdoption;
}
