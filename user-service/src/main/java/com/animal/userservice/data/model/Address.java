package com.animal.userservice.data.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
}
