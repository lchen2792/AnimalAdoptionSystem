package com.animal.animalservice.exception;

import com.animal.animalservice.data.model.AnimalStatus;

public class AnimalStatusNotMatchException extends RuntimeException{
    public AnimalStatusNotMatchException(String animalProfileId, AnimalStatus expectedStatus, AnimalStatus actualStatus) {
        super(String
                .format("animal %s expected status %s actual status %s",
                        animalProfileId,
                        expectedStatus.name(),
                        actualStatus.name()
                )
        );
    }
}
