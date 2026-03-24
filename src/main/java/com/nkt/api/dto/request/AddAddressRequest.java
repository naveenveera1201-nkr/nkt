package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddAddressRequest {
    @NotBlank private String label;
    @NotBlank private String line1;
    private String line2;
    @NotBlank private String city;
    @NotBlank private String state;
    @NotBlank @Pattern(regexp="^[0-9]{6}$") private String pincode;
    @DecimalMin("-90.0") @DecimalMax("90.0") private Double latitude;
    @DecimalMin("-180.0") @DecimalMax("180.0") private Double longitude;
}
