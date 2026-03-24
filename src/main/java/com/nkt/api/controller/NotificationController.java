package com.nkt.api.controller;

import com.nkt.api.dto.request.RegisterDeviceRequest;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /** API 33 – Register Device for Push Notifications */
    @PostMapping("/register-device")
    public ResponseEntity<ApiResponse<Object>> registerDevice(
            @Valid @RequestBody RegisterDeviceRequest req,
            @AuthenticationPrincipal String userId) {
        var result = notificationService.registerDevice(req, userId);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }
}
