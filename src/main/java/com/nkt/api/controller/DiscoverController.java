package com.nkt.api.controller;

import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.DiscoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DiscoverController {

    private final DiscoverService discoverService;

    /** API 12 – List All Categories */
    @GetMapping("/api/v1/categories")
    public ResponseEntity<ApiResponse<Object>> listCategories() {
        var cats = discoverService.listCategories();
        return ResponseEntity.ok(ApiResponse.success(cats, "ACTIVE"));
    }

    /** API 13 – Get Hero Banner */
    @GetMapping("/api/v1/discover/banner")
    public ResponseEntity<ApiResponse<Object>> getHeroBanner() {
        var banners = discoverService.getHeroBanner();
        return ResponseEntity.ok(ApiResponse.success(banners, "ACTIVE"));
    }

    /** API 14 – Get Nearby Stores Strip */
    @GetMapping("/api/v1/discover/nearby")
    public ResponseEntity<ApiResponse<Object>> getNearbyStores(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(required = false) Double radiusKm) {
        var stores = discoverService.getNearbyStores(latitude, longitude, radiusKm);
        return ResponseEntity.ok(ApiResponse.success(stores, "ACTIVE"));
    }
}
