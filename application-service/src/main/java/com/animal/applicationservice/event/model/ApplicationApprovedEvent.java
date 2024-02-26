package com.animal.applicationservice.event.model;

import com.animal.applicationservice.data.model.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ApplicationApprovedEvent implements Serializable {
    private String applicationId;
    private ApplicationStatus applicationStatus;
}
