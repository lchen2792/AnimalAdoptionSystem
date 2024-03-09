package com.animal.animalservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Temperament implements Serializable {
    private Level sociability;
    private Level activity;
    private Level trainability;
    private Level stability;
    private Level aggressivity;
    private Level independency;
    private Level adaptability;
    private Level preyDrive;
    private Level communication;
}
