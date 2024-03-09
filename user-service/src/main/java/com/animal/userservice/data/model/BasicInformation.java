package com.animal.userservice.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BasicInformation {
    private Name name;
    private Address address;
    private Contact contact;
}
