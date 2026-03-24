package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddStockItemRequest {
    @NotBlank private String name;
    private String sub;
    @DecimalMin("0.01") private double price;
    private String emoji;
    @NotBlank private String category;
    @Min(0) private int qty;
}
