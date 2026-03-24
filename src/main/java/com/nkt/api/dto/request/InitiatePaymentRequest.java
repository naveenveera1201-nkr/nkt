package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class InitiatePaymentRequest {
    @NotBlank private String orderId;
    @Min(1) private long amount;
    @NotBlank @Pattern(regexp="^(upi|netbanking)$") private String method;
    private String upiId;
    private String bankCode;
}
