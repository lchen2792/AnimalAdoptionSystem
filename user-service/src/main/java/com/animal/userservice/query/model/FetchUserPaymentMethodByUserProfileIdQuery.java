package com.animal.userservice.query.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FetchUserPaymentMethodByUserProfileIdQuery implements Serializable {
    private String userProfileId;
}
