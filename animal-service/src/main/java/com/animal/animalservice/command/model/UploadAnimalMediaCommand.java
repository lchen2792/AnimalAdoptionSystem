package com.animal.animalservice.command.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UploadAnimalMediaCommand implements Serializable {
    private String animalProfileId;
    private String mediaId;
}
