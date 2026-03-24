package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BiometricEnrolRequest {
    @NotBlank private String deviceId;
    @NotBlank private String biometricToken;
    @NotBlank @Pattern(regexp="^(android|ios)$") private String platform;
}
