package com.nkt.api.controller;

import com.nkt.api.dto.request.InitiatePaymentRequest;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /** API 27 – Initiate Payment */
    @PostMapping("/initiate")
    public ResponseEntity<ApiResponse<Object>> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequest req,
            @AuthenticationPrincipal String userId) {
        var payment = paymentService.initiatePayment(req, userId);
        return ResponseEntity.ok(ApiResponse.created(payment.getId(), payment));
    }

    /** API 28 – Check Payment Status */
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<ApiResponse<Object>> checkStatus(
            @PathVariable String paymentId,
            @AuthenticationPrincipal String userId) {
        var payment = paymentService.checkPaymentStatus(paymentId, userId);
        return ResponseEntity.ok(ApiResponse.success(payment, payment.getCurrentStatus()));
    }

    /** API 29 – Payment Gateway Webhook (public) */
    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<Object>> webhook(@RequestBody Map<String, Object> payload) {
        var result = paymentService.handleWebhook(payload);
        return ResponseEntity.ok(ApiResponse.success(result, "RECEIVED"));
    }
}
