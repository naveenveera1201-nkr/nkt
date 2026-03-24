package com.nkt.api.controller;

import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.StoresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoresController {

    private final StoresService storesService;

    /** API 15 – List Stores by Category */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> listStores(
            @RequestParam String categoryId,
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude) {
        var stores = storesService.listByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(stores, "ACTIVE"));
    }

    /** API 16 – Get Store Details */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResponse<Object>> getStoreDetails(@PathVariable String storeId) {
        var store = storesService.getStoreDetails(storeId);
        return ResponseEntity.ok(ApiResponse.success(store, "ACTIVE"));
    }

    /** API 17 – Get Store Product Catalogue */
    @GetMapping("/{storeId}/products")
    public ResponseEntity<ApiResponse<Object>> getProducts(
            @PathVariable String storeId,
            @RequestParam(required = false) String categoryFilter,
            @RequestParam(required = false) Boolean available) {
        var products = storesService.getProducts(storeId, categoryFilter, available);
        return ResponseEntity.ok(ApiResponse.success(products, "ACTIVE"));
    }

    /** API 18 – Get Store Services */
    @GetMapping("/{storeId}/services")
    public ResponseEntity<ApiResponse<Object>> getServices(@PathVariable String storeId) {
        var services = storesService.getServices(storeId);
        return ResponseEntity.ok(ApiResponse.success(services, "ACTIVE"));
    }

    /** API 19 – Get Available Time Slots */
    @GetMapping("/{storeId}/availability")
    public ResponseEntity<ApiResponse<Object>> getAvailability(
            @PathVariable String storeId,
            @RequestParam String serviceId,
            @RequestParam String fromDate,
            @RequestParam(required = false) String toDate) {
        var slots = storesService.getAvailability(storeId, serviceId, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success(slots, "ACTIVE"));
    }
}
