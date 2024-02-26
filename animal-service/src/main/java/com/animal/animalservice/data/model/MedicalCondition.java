package com.animal.animalservice.data.model;

import java.io.Serializable;
import java.util.List;

public class MedicalCondition implements Serializable {
    private String causes;
    private String symptoms;
    private String cautions;
    private Boolean cured;
    private List<String> documents;
}
