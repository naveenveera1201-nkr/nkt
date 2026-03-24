package com.nkt.api.controller;

import com.nkt.api.dto.request.AddAddressRequest;
import com.nkt.api.dto.request.UpdateProfileRequest;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /** API 07 – Get Customer Profile */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> getProfile(@AuthenticationPrincipal String userId) {
        var profile = customerService.getProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile, "ACTIVE"));
    }

    /** API 08 – Update Customer Profile */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Object>> updateProfile(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody UpdateProfileRequest req) {
        var updated = customerService.updateProfile(userId, req);
        return ResponseEntity.ok(ApiResponse.updated(updated.getId(), updated));
    }

    /** API 09 – Add Delivery Address */
    @PostMapping("/addresses")
    public ResponseEntity<ApiResponse<Object>> addAddress(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AddAddressRequest req) {
        var address = customerService.addAddress(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(address.getId(), address));
    }

    /** API 10 – Delete Saved Address */
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal String userId,
            @PathVariable String addressId) {
        customerService.deleteAddress(userId, addressId);
        return ResponseEntity.ok(ApiResponse.deleted(addressId));
    }

    /** API 11 – Save / Unsave Favourite Store */
    @PostMapping("/favourites/{storeId}")
    public ResponseEntity<ApiResponse<Object>> toggleFavourite(
            @AuthenticationPrincipal String userId,
            @PathVariable String storeId) {
        var result = customerService.toggleFavourite(userId, storeId);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }
}
