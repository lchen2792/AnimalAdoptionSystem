package com.animal.animalservice.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchAnimalProfilesByCriteriaQuery implements Serializable {
    private String species;
    private String breed;
    private Integer ageMin;
    private Integer ageMax;
    private String gender;
    private Boolean neutered;
}
