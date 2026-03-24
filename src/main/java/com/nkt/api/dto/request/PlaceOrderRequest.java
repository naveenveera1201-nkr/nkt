package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PlaceOrderRequest {
    @NotBlank private String storeId;
    @NotBlank private String categoryId;
    @NotEmpty private List<OrderItemDto> items;
    @NotBlank private String addressId;
    @NotBlank @Pattern(regexp="^(upi|netbanking|cod|wallet)$") private String paymentMethod;
    private String appointmentSlot;
    private String notes;
    @Pattern(regexp="^(normal|urgent|express)$") private String urgency;
    @Pattern(regexp="^(delivery|pickup|appointment)$") private String orderType;

    @Data
    public static class OrderItemDto {
        @NotBlank private String itemId;
        @Min(1) private int qty;
    }
}
