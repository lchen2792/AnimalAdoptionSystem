package com.animal.animalservice.event.model;

import com.animal.animalservice.data.model.AnimalStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AnimalReleasedEvent implements Serializable {
    private String animalProfileId;
    private String applicationId;
    private String userProfileId;
    private AnimalStatus status;
    private String message;
}
