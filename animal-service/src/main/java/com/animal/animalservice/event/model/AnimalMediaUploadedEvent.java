package com.animal.animalservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalMediaUploadedEvent implements Serializable {
    private String animalProfileId;
    private String mediaId;
}
