package com.nkt.api.controller;

import com.nkt.api.dto.request.ToggleStoreStatusRequest;
import com.nkt.api.dto.request.UpdateStoreProfileRequest;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.StoreProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreProfileController {

    private final StoreProfileService storeProfileService;

    /** API 36 – Get Store Profile */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getProfile(@AuthenticationPrincipal String userId) {
        var store = storeProfileService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(store, "ACTIVE"));
    }

    /** API 37 – Update Store Profile */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> updateProfile(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateStoreProfileRequest req) {
        var store = storeProfileService.updateProfile(userId, req);
        return ResponseEntity.ok(ApiResponse.updated(store.getId(), store));
    }

    /** API 38 – Toggle Store Open / Closed */
    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<Object>> toggleStatus(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody ToggleStoreStatusRequest req) {
        var result = storeProfileService.toggleStatus(userId, req);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 39 – Get Dashboard Stats */
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<Object>> getDashboard(@AuthenticationPrincipal String userId) {
        var dashboard = storeProfileService.getDashboard(userId);
        return ResponseEntity.ok(ApiResponse.success(dashboard, "ACTIVE"));
    }
}
