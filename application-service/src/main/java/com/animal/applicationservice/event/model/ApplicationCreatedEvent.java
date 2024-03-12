package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationCreatedEvent implements Serializable {
    private String applicationId;
    private String userProfileId;
    private String animalProfileId;
    private ApplicationStatus applicationStatus;
}
