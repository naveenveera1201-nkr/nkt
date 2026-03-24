package com.nkt.api.controller;

import com.nkt.api.dto.request.*;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    /** API 40 – Get Full Stock List */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getStockList(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean available) {
        var items = stockService.getStockList(userId, category, available);
        return ResponseEntity.ok(ApiResponse.success(items, "ACTIVE"));
    }

    /** API 41 – Add New Stock Item */
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> addItem(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody AddStockItemRequest req) {
        var item = stockService.addItem(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(item.getId(), item));
    }

    /** API 42 – List Stock Sub-categories */
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<Object>> listCategories(@AuthenticationPrincipal String userId) {
        var cats = stockService.listCategories(userId);
        return ResponseEntity.ok(ApiResponse.success(cats, "ACTIVE"));
    }

    /** API 43 – Update Stock Item (Full Update) */
    @PutMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Object>> updateItem(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId,
            @Valid @RequestBody UpdateStockItemRequest req) {
        var item = stockService.updateItem(userId, itemId, req);
        return ResponseEntity.ok(ApiResponse.updated(item.getId(), item));
    }

    /** API 44 – Adjust Stock Quantity (Delta) */
    @PatchMapping("/{itemId}/adjust")
    public ResponseEntity<ApiResponse<Object>> adjustQty(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId,
            @Valid @RequestBody AdjustStockRequest req) {
        var item = stockService.adjustQty(userId, itemId, req);
        return ResponseEntity.ok(ApiResponse.updated(item.getId(), item));
    }

    /** API 45 – Toggle Item Availability */
    @PatchMapping("/{itemId}/toggle")
    public ResponseEntity<ApiResponse<Object>> toggleAvailability(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId,
            @Valid @RequestBody ToggleAvailabilityRequest req) {
        var item = stockService.toggleAvailability(userId, itemId, req);
        return ResponseEntity.ok(ApiResponse.updated(item.getId(), item));
    }

    /** API 46 – Remove Custom Stock Item */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Object>> deleteItem(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId) {
        var result = stockService.deleteItem(userId, itemId);
        return ResponseEntity.ok(ApiResponse.success(result, "DELETED"));
    }
}
