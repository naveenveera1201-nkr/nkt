package com.nkt.api.dto.request;
import lombok.Data;
import java.util.Map;

@Data
public class UpdateStoreProfileRequest {
    private String name;
    private String phone;
    private Map<String, Map<String, String>> hours;
}
