package com.nkt.api.controller;

import com.nkt.api.dto.request.*;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.CartOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CartOrderController {

    private final CartOrderService cartOrderService;

    /** API 20 – Validate Cart Before Checkout */
    @PostMapping("/api/v1/cart/validate")
    public ResponseEntity<ApiResponse<Object>> validateCart(
            @Valid @RequestBody ValidateCartRequest req,
            @AuthenticationPrincipal String userId) {
        var result = cartOrderService.validateCart(req);
        return ResponseEntity.ok(ApiResponse.success(result, "VALID"));
    }

    /** API 21 – Place New Order */
    @PostMapping("/api/v1/orders")
    public ResponseEntity<ApiResponse<Object>> placeOrder(
            @Valid @RequestBody PlaceOrderRequest req,
            @AuthenticationPrincipal String userId) {
        var order = cartOrderService.placeOrder(req, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(order.getId(), order));
    }

    /** API 22 – Get Customer Order History */
    @GetMapping("/api/v1/orders")
    public ResponseEntity<ApiResponse<Object>> getOrderHistory(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        var orders = cartOrderService.getOrderHistory(userId, status, page, limit);
        return ResponseEntity.ok(ApiResponse.success(orders, "ACTIVE"));
    }

    /** API 23 – Get Order Detail */
    @GetMapping("/api/v1/orders/{orderId}")
    public ResponseEntity<ApiResponse<Object>> getOrderDetail(
            @PathVariable String orderId,
            @AuthenticationPrincipal String userId) {
        var order = cartOrderService.getOrderDetail(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(order, order.getCurrentStatus()));
    }

    /** API 24 – Real-time Order Tracking (REST poll) */
    @GetMapping("/api/v1/orders/{orderId}/track")
    public ResponseEntity<ApiResponse<Object>> trackOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal String userId) {
        var tracking = cartOrderService.trackOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(tracking, "ACTIVE"));
    }

    /** API 25 – Cancel Order */
    @PostMapping("/api/v1/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Object>> cancelOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal String userId,
            @RequestBody(required = false) CancelOrderRequest req) {
        var order = cartOrderService.cancelOrder(orderId, userId,
                req != null ? req : new CancelOrderRequest());
        return ResponseEntity.ok(ApiResponse.success(order, "CANCELLED"));
    }

    /** API 26 – Rate Order & Store */
    @PostMapping("/api/v1/orders/{orderId}/rate")
    public ResponseEntity<ApiResponse<Object>> rateOrder(
            @PathVariable String orderId,
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody RateOrderRequest req) {
        var rating = cartOrderService.rateOrder(orderId, userId, req);
        return ResponseEntity.ok(ApiResponse.created(rating.getId(), rating));
    }
}
