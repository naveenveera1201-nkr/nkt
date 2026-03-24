package com.nkt.api.service;

import com.nkt.api.dto.request.*;
import com.nkt.api.model.*;
import com.nkt.api.repository.*;
import com.nkt.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OtpRepository       otpRepository;
    private final UserRepository      userRepository;
    private final BiometricTokenRepository biometricRepo;
    private final JwtTokenProvider    jwtTokenProvider;

    // API 1 – Send OTP
    public Map<String, Object> sendOtp(SendOtpRequest req) {
       
    	if ("register".equals(req.getPurpose()) && userRepository.existsByIdentifier(req.getIdentifier()))
            throw new RuntimeException("User already exists");

        String otp = String.format("%04d", new Random().nextInt(10000));
        otpRepository.deleteByIdentifier(req.getIdentifier());

        OtpRecord record = OtpRecord.builder()
                .identifier(req.getIdentifier())
                .identifierType(req.getIdentifierType())
                .otp(otp)
                .userType(req.getUserType())
                .purpose(req.getPurpose())
                .used(false)
                .attempts(0)
                .createdAt(LocalDateTime.now())
                .build();
        otpRepository.save(record);

        // TODO: integrate SMS/email gateway to deliver otp
        return Map.of("message", "OTP sent successfully", "expiresInSeconds", 300);
    }

    // API 2 – Verify OTP & Issue Token
    public Map<String, String> verifyOtp(VerifyOtpRequest req) {
        OtpRecord record = otpRepository
                .findTopByIdentifierAndUsedFalseOrderByCreatedAtDesc(req.getIdentifier())
                .orElseThrow(() -> new RuntimeException("OTP not found or expired"));

        if (!record.getOtp().equals(req.getOtp()))
            throw new RuntimeException("Invalid OTP");

        record.setUsed(true);
        record.setVerifiedAt(LocalDateTime.now());
        otpRepository.save(record);

        User user;
        if ("register".equals(req.getPurpose())) {
            user = User.builder()
                    .identifier(req.getIdentifier())
                    .identifierType(record.getIdentifierType())
                    .name(req.getName())
                    .email(req.getEmail())
                    .userType(req.getUserType())
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .createdBy("SYSTEM")
                    .build();
            userRepository.save(user);
        } else {
            user = userRepository.findByIdentifier(req.getIdentifier())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        String accessToken  = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken, "userType", user.getUserType());
    }

    // API 3 – Refresh Access Token
    public Map<String, String> refreshToken(RefreshTokenRequest req) {
        if (!jwtTokenProvider.isTokenValid(req.getRefreshToken()))
            throw new RuntimeException("Invalid or expired refresh token");

        String userId = jwtTokenProvider.extractUserId(req.getRefreshToken());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccess = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType());
        return Map.of("accessToken", newAccess);
    }

    // API 4 – Enrol Biometric Token
    public Map<String, String> enrolBiometric(BiometricEnrolRequest req, String userId) {
        BiometricToken token = BiometricToken.builder()
                .userId(userId)
                .deviceId(req.getDeviceId())
                .tokenHash(req.getBiometricToken())  // hash before storing in production
                .platform(req.getPlatform())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        biometricRepo.save(token);
        return Map.of("message", "Biometric token enrolled successfully");
    }

    // API 5 – Sign In via Biometric
    public Map<String, String> verifyBiometric(BiometricVerifyRequest req) {
        BiometricToken stored = biometricRepo
                .findByDeviceIdAndStatus(req.getDeviceId(), "ACTIVE")
                .orElseThrow(() -> new RuntimeException("Device not enrolled"));

        if (!stored.getTokenHash().equals(req.getBiometricToken()))
            throw new RuntimeException("Biometric verification failed");

        User user = userRepository.findById(stored.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken  = jwtTokenProvider.generateAccessToken(user.getId(), user.getUserType());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
    }

    // API 6 – Logout
    public Map<String, String> logout(LogoutRequest req) {
        // In production: add refreshToken to a deny-list / Redis blacklist
        return Map.of("message", "Logged out successfully");
    }
}
