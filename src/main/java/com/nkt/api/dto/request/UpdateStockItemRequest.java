package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateStockItemRequest {
    private String name;
    @DecimalMin("0.01") private Double price;
    @Min(0) private Integer qty;
    private Boolean available;
    private String sub;
    private String emoji;
}
