package com.nkt.api.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustStockRequest {
    @NotNull private Integer delta;
}
