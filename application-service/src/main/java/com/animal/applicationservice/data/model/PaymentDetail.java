package com.animal.applicationservice.data.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class PaymentDetail implements Serializable {
    private String userId;
    private String cardholderName;
    private String cardNumber;
    private Integer validThruYear;
    private Integer validThruMonth;
    private Integer CVV;
}
