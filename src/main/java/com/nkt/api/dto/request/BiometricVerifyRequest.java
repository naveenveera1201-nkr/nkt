package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BiometricVerifyRequest {
    @NotBlank private String deviceId;
    @NotBlank private String biometricToken;
    @NotBlank @Pattern(regexp="^(customer|store)$") private String userType;
}
