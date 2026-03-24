package com.nkt.api.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ToggleAvailabilityRequest {
    @NotNull private Boolean available;
}
