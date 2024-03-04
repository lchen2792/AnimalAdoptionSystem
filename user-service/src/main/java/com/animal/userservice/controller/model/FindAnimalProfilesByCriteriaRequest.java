package com.animal.userservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FindAnimalProfilesByCriteriaRequest {
    private String species;
    private String breed;
    private Integer ageMin;
    private Integer ageMax;
    private String gender;
    private Boolean neutered;
}
