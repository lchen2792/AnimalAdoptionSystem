package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LivingSituation {
    private String typeOfResidence;
    private Level availableSpace;
}
