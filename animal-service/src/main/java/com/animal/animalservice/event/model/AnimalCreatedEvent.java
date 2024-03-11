package com.animal.animalservice.event.model;

import com.animal.animalservice.data.model.*;
import com.animal.common.status.AnimalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalCreatedEvent implements Serializable {
    private String animalProfileId;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<String> media = new ArrayList<>();
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
    private AnimalStatus status;
}
