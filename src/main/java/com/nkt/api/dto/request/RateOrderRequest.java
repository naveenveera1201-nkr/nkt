package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RateOrderRequest {
    @NotNull @Min(1) @Max(5) private Integer rating;
    @Size(max=1000) private String review;
}
