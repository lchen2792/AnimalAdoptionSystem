package com.animal.animalservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class VeterinaryRecord implements Serializable {
    private String type;
    private String time;
    private List<String> prognosis;
    private List<String> diagnosis;
    private List<String> treatment;
    private List<String> documents;
}
