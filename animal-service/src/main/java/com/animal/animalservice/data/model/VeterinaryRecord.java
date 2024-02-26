package com.animal.animalservice.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class VeterinaryRecord implements Serializable {
    private String type;
    private String time;
    private List<String> prognosis;
    private List<String> diagnosis;
    private List<String> treatment;
    private List<String> documents;
}
