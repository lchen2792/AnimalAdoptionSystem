package com.animal.userservice.controller.model;

import com.animal.userservice.data.model.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileForMatch {
    private String userProfileId;
    private LivingSituation livingSituation;
    private FamilySituation familySituation;
    private Experience experience;
    private Knowledge knowledge;
    private Personality personality;
}
