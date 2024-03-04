package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FamilySituation {
    private Integer numberOfAdults;
    private Integer numberOfChildren;
    private List<Pet> pets;
}
