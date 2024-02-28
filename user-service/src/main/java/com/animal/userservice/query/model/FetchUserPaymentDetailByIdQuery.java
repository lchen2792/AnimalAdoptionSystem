package com.animal.userservice.query.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FetchUserPaymentDetailByIdQuery implements Serializable {
    private String userId;
}
