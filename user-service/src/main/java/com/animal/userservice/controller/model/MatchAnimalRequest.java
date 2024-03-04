package com.animal.userservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchAnimalRequest {
    private UserProfileForMatch userProfileForMatch;
    private FindAnimalProfilesByCriteriaRequest request;
}
