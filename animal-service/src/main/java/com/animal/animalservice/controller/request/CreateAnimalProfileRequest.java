package com.animal.animalservice.controller.request;

import com.animal.animalservice.data.model.*;
import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;

import java.util.List;

@Data
@Builder
public class CreateAnimalProfileRequest {
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
}
