package com.nkt.api.dto.request;
import lombok.Data;

@Data
public class DispatchOrderRequest {
    private String agentName;
    private String agentPhone;
    private String vehiclePlate;
}
