package com.animal.userservice.controller.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidatePaymentMethodRequest {
    private String cardholderName;
    private String cardNumber;
    private Integer validThruYear;
    private Integer validThruMonth;
    private Integer CVV;
}
