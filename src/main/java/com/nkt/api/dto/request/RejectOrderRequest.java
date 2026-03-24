package com.nkt.api.dto.request;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RejectOrderRequest {
    @Pattern(regexp="^(out_of_stock|store_closed|cannot_fulfil|other)$")
    private String reason;
}
