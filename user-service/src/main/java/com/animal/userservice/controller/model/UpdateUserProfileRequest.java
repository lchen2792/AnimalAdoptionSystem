package com.animal.userservice.controller.model;

import com.animal.userservice.data.model.*;
import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.io.Serializable;

@Data
@Builder
public class UpdateUserProfileRequest implements Serializable {
    private String userProfileId;
    private BasicInformation basicInformation;
    private LivingSituation livingSituation;
    private FamilySituation familySituation;
    private Experience experience;
    private Knowledge knowledge;
    private Personality personality;
}
