package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ApplicationCreatedEvent implements Serializable {
    private String applicationId;
    private String userProfileId;
    private String animalProfileId;
    private ApplicationStatus applicationStatus;
}
