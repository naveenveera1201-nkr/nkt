package com.nkt.api.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AcceptOrderRequest {
    @NotNull private Boolean partial;
    private List<Map<String, Object>> fulfilledItems;
    private String storeNote;
}
