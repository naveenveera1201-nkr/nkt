package com.nkt.api.dto.request;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    @Email private String email;
}
