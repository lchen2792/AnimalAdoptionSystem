package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pet {
    private String species;
    private String breed;
    private Integer age;
}
