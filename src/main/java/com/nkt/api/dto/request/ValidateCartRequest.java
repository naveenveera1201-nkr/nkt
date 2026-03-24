package com.nkt.api.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ValidateCartRequest {
    @NotBlank private String storeId;
    @NotEmpty private List<Map<String, Object>> items;
}
