package com.animal.animalservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CareRequirements implements Serializable {
    private Level space;
    private Level socializing;
    private Level companionship;
    private Level exercise;
    private Level grooming;
    private Level diet;
}
