package com.animal.applicationservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewApplicationRequest {
    private String applicationId;
    private Boolean approve;
    private String comment;
}
