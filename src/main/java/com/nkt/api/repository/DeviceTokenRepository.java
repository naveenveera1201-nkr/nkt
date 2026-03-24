package com.nkt.api.repository;

import com.nkt.api.model.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface DeviceTokenRepository extends MongoRepository<DeviceToken, String> {
    Optional<DeviceToken> findByUserIdAndDeviceToken(String userId, String deviceToken);
}
