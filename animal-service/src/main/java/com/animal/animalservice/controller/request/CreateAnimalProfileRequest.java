package com.animal.animalservice.controller.request;

import com.animal.animalservice.data.model.*;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CreateAnimalProfileRequest implements Serializable {
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
}
