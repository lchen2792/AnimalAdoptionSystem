package com.animal.animalservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BasicInformation implements Serializable {
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private Size size;
    private Boolean neutered;
}
