package com.animal.applicationservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateApplicationRequest {
    private String userProfileId;
    private String animalProfileId;
}
