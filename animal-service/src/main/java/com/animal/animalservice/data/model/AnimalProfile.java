package com.animal.animalservice.data.model;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AnimalProfile implements Serializable {
    @Id
    private String animalProfileId;
    private AnimalStatus status;
    private BasicInformation basicInformation;
    private Temperament temperament;
    private CareRequirements careRequirements;
    private List<String> photos;
    private List<MedicalCondition> medicalConditions;
    private List<VeterinaryRecord> veterinaryRecords;
    @Version
    private Long version;
    @CreatedDate
    private Long createdDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private Long lastModifiedDate;
}
