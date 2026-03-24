package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDeviceRequest {
    @NotBlank private String deviceToken;
    @NotBlank @Pattern(regexp="^(android|ios)$") private String platform;
    @NotBlank @Pattern(regexp="^(customer|store)$") private String userType;
}
