package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.AnimalStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AnimalReleasedForRejectionEvent implements Serializable {
    private String animalProfileId;
    private String applicationId;
    private String userProfileId;
    private AnimalStatus status;
    private String message;
}
