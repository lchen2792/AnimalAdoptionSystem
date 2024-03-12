package com.animal.common.event;

import com.animal.common.status.AnimalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalReleasedEvent implements Serializable {
    private String animalProfileId;
    private String applicationId;
    private String userProfileId;
    private String message;
    private AnimalStatus status;
}
