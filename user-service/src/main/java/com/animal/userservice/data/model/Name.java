package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Name {
    private String firstName;
    private String middleName;
    private String lastName;
}
