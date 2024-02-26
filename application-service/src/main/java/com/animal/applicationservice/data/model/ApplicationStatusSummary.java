package com.animal.applicationservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ApplicationStatusSummary implements Serializable {
    private String applicationId;
    private ApplicationStatus applicationStatus;
    private String message;
}
