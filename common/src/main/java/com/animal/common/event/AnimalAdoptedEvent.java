package com.animal.common.event;

import com.animal.common.status.AnimalStatus;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AnimalAdoptedEvent implements Serializable {
    private String animalProfileId;
    private String applicationId;
    private String userProfileId;
    private AnimalStatus status;
}
