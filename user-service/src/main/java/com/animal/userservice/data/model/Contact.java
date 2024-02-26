package com.animal.userservice.data.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Contact {
    private String phone;
    private String email;
}
