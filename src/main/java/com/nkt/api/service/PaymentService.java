package com.nkt.api.service;

import com.nkt.api.dto.request.InitiatePaymentRequest;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Payment;
import com.nkt.api.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    // API 27 – Initiate Payment
    public Payment initiatePayment(InitiatePaymentRequest req, String customerId) {
        String redirectUrl = "upi".equals(req.getMethod())
                ? "upi://pay?pa=" + req.getUpiId() + "&am=" + (req.getAmount() / 100.0)
                : "https://netbanking.example.com/redirect?bank=" + req.getBankCode();

        Payment payment = Payment.builder()
                .orderId(req.getOrderId()).customerId(customerId)
                .amount(req.getAmount()).method(req.getMethod())
                .upiId(req.getUpiId()).bankCode(req.getBankCode())
                .status("pending").currentStatus("pending")
                .redirectUrl(redirectUrl)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .build();
        return paymentRepository.save(payment);
    }

    // API 28 – Check Payment Status
    public Payment checkPaymentStatus(String paymentId, String customerId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
    }

    // API 29 – Payment Gateway Webhook
    public Map<String, String> handleWebhook(Map<String, Object> payload) {
        // TODO: verify signature, update payment & order status
        String gatewayPaymentId = String.valueOf(payload.getOrDefault("paymentId", ""));
        String status           = String.valueOf(payload.getOrDefault("status", "unknown"));
        return Map.of("received", "true", "gatewayPaymentId", gatewayPaymentId, "status", status);
    }
}
