package com.animal.animalservice.command.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class DeleteAnimalMediaCommand implements Serializable {
    private String animalProfileId;
    private String mediaId;
}
