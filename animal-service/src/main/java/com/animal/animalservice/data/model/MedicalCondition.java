package com.animal.animalservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class MedicalCondition implements Serializable {
    private String causes;
    private String symptoms;
    private String cautions;
    private Boolean cured;
    private List<String> documents;
}
