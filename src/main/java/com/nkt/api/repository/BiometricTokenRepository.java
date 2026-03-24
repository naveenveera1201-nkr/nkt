package com.nkt.api.repository;

import com.nkt.api.model.BiometricToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface BiometricTokenRepository extends MongoRepository<BiometricToken, String> {
    Optional<BiometricToken> findByUserIdAndDeviceIdAndStatus(String userId, String deviceId, String status);
    Optional<BiometricToken> findByDeviceIdAndStatus(String deviceId, String status);
}
