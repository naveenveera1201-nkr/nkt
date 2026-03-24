package com.nkt.api.controller;

import com.nkt.api.dto.request.*;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.StoreOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/store/orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;

    /** API 47 – Get All Store Orders */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getAllOrders(
            @AuthenticationPrincipal String userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int limit) {
        var orders = storeOrderService.getAllOrders(userId, status, page, limit);
        return ResponseEntity.ok(ApiResponse.success(orders, "ACTIVE"));
    }

    /** API 48 – Accept Order — Full or Partial */
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<ApiResponse<Object>> acceptOrder(
            @AuthenticationPrincipal String userId,
            @PathVariable String orderId,
            @Valid @RequestBody AcceptOrderRequest req) {
        var order = storeOrderService.acceptOrder(userId, orderId, req);
        return ResponseEntity.ok(ApiResponse.updated(order.getId(), order));
    }

    /** API 49 – Reject Order Entirely */
    @PostMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<Object>> rejectOrder(
            @AuthenticationPrincipal String userId,
            @PathVariable String orderId,
            @RequestBody(required = false) RejectOrderRequest req) {
        var order = storeOrderService.rejectOrder(userId, orderId,
                req != null ? req : new RejectOrderRequest());
        return ResponseEntity.ok(ApiResponse.updated(order.getId(), order));
    }

    /** API 50 – Mark Order as Dispatched */
    @PostMapping("/{orderId}/dispatch")
    public ResponseEntity<ApiResponse<Object>> dispatchOrder(
            @AuthenticationPrincipal String userId,
            @PathVariable String orderId,
            @RequestBody(required = false) DispatchOrderRequest req) {
        var order = storeOrderService.dispatchOrder(userId, orderId,
                req != null ? req : new DispatchOrderRequest());
        return ResponseEntity.ok(ApiResponse.updated(order.getId(), order));
    }

    /** API 51 – Mark Order as Delivered */
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<ApiResponse<Object>> deliverOrder(
            @AuthenticationPrincipal String userId,
            @PathVariable String orderId,
            @RequestBody(required = false) DeliverOrderRequest req) {
        var order = storeOrderService.deliverOrder(userId, orderId,
                req != null ? req : new DeliverOrderRequest());
        return ResponseEntity.ok(ApiResponse.updated(order.getId(), order));
    }
}
