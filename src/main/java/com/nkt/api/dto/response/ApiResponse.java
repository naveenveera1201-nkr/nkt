package com.nkt.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String id;
    private String history;
    private T data;
    private String currentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String message;

    // ── Convenience factories ─────────────────────────────────────────────────

    public static <T> ApiResponse<T> success(T data, String status) {
        return ApiResponse.<T>builder()
                .data(data)
                .currentStatus(status)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> created(String id, T data) {
        return ApiResponse.<T>builder()
                .id(id)
                .data(data)
                .currentStatus("ACTIVE")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> updated(String id, T data) {
        return ApiResponse.<T>builder()
                .id(id)
                .data(data)
                .currentStatus("ACTIVE")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Void> deleted(String id) {
        return ApiResponse.<Void>builder()
                .id(id)
                .currentStatus("DELETED")
                .message("Resource deleted successfully")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ApiResponse<Void> message(String msg) {
        return ApiResponse.<Void>builder()
                .message(msg)
                .currentStatus("ACTIVE")
                .build();
    }
}
