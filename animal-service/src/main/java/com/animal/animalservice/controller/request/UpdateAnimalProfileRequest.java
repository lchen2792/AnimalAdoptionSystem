package com.animal.animalservice.controller.request;

import com.animal.animalservice.data.model.*;
import com.animal.common.status.AnimalStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateAnimalProfileRequest {
    private String animalProfileId;
    private AnimalStatus status;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
}
