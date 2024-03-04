package com.animal.userservice.controller.model;

import com.animal.userservice.data.model.Level;
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
