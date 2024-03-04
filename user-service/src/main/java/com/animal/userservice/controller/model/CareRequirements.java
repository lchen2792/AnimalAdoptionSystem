package com.animal.userservice.controller.model;

import com.animal.userservice.data.model.Level;
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
