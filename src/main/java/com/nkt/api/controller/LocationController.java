package com.nkt.api.controller;

import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /** API 34 – Reverse Geocode Location */
    @GetMapping("/api/v1/location/geocode")
    public ResponseEntity<ApiResponse<Object>> reverseGeocode(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        var result = locationService.reverseGeocode(latitude, longitude);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 35 – Global Search */
    @GetMapping("/api/v1/search")
    public ResponseEntity<ApiResponse<Object>> globalSearch(
            @RequestParam String q,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String categoryId) {
        var result = locationService.globalSearch(q, latitude, longitude, categoryId);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }
}
