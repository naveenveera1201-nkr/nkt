package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class VerifyOtpRequest {
    @NotBlank private String identifier;
    @NotBlank @Size(min=4,max=4) @Pattern(regexp="^[0-9]{4}$") private String otp;
    @NotBlank @Pattern(regexp="^(customer|store)$") private String userType;
    @NotBlank @Pattern(regexp="^(register|login)$") private String purpose;
    private String name;
    @Email private String email;
}
