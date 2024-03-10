package com.animal.userservice.controller.model;

import com.animal.userservice.data.model.*;
import com.animal.userservice.data.model.BasicInformation;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class CreateUserProfileRequest implements Serializable {
    private BasicInformation basicInformation;
    private LivingSituation livingSituation;
    private FamilySituation familySituation;
    private Experience experience;
    private Knowledge knowledge;
    private Personality personality;
}
