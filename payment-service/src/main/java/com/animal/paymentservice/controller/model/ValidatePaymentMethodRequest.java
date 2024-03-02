package com.animal.paymentservice.controller.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

@Data
@Builder
public class ValidatePaymentMethodRequest {
    @NotBlank
    private String cardholderName;
    @NotBlank
    private String cardNumber;
    @Min(0)
    private Integer validThruYear;
    @Min(1)
    @Max(12)
    private Integer validThruMonth;
    @Min(0)
    @Max(999)
    private Integer CVV;
}
