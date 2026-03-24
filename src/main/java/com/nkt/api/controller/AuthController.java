package com.nkt.api.controller;

import com.nkt.api.dto.request.*;
import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /** API 01 – Send OTP */
    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<Object>> sendOtp(@Valid @RequestBody SendOtpRequest req) {
        var result = authService.sendOtp(req);
        return ResponseEntity.ok(ApiResponse.success(result, "OTP_SENT"));
    }

    /** API 02 – Verify OTP & Issue Token */
    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Object>> verifyOtp(@Valid @RequestBody VerifyOtpRequest req) {
        var result = authService.verifyOtp(req);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 03 – Refresh Access Token */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Object>> refreshToken(@Valid @RequestBody RefreshTokenRequest req) {
        var result = authService.refreshToken(req);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 04 – Enrol Biometric Token */
    @PostMapping("/biometric/enrol")
    public ResponseEntity<ApiResponse<Object>> enrolBiometric(
            @Valid @RequestBody BiometricEnrolRequest req,
            @AuthenticationPrincipal String userId) {
        var result = authService.enrolBiometric(req, userId);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 05 – Sign In via Biometric */
    @PostMapping("/biometric/verify")
    public ResponseEntity<ApiResponse<Object>> verifyBiometric(@Valid @RequestBody BiometricVerifyRequest req) {
        var result = authService.verifyBiometric(req);
        return ResponseEntity.ok(ApiResponse.success(result, "ACTIVE"));
    }

    /** API 06 – Logout / Revoke Token */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @Valid @RequestBody LogoutRequest req,
            @AuthenticationPrincipal String userId) {
        var result = authService.logout(req);
        return ResponseEntity.ok(ApiResponse.success(result, "LOGGED_OUT"));
    }
}
