package com.nkt.api.service;

import com.nkt.api.dto.request.RegisterDeviceRequest;
import com.nkt.api.model.DeviceToken;
import com.nkt.api.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final DeviceTokenRepository deviceTokenRepository;

    // API 33 – Register Device for Push Notifications
    public Map<String, String> registerDevice(RegisterDeviceRequest req, String userId) {
        DeviceToken existing = deviceTokenRepository
                .findByUserIdAndDeviceToken(userId, req.getDeviceToken())
                .orElse(null);
        if (existing != null) {
            existing.setPlatform(req.getPlatform());
            existing.setStatus("ACTIVE");
            existing.setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(existing);
        } else {
            DeviceToken dt = DeviceToken.builder()
                    .userId(userId).deviceToken(req.getDeviceToken())
                    .platform(req.getPlatform()).userType(req.getUserType())
                    .status("ACTIVE").createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now()).build();
            deviceTokenRepository.save(dt);
        }
        return Map.of("message", "Device registered successfully");
    }

    // Helper: send push notification (FCM/APNs stub)
    public void sendPushNotification(String userId, String title, String body) {
        deviceTokenRepository.findByUserIdAndDeviceToken(userId, userId)
                .ifPresent(dt -> {
                    // TODO: integrate FCM/APNs SDK
                });
    }
}
