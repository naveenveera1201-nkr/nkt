package com.nkt.api.repository;

import com.nkt.api.model.OtpRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface OtpRepository extends MongoRepository<OtpRecord, String> {
    Optional<OtpRecord> findTopByIdentifierAndUsedFalseOrderByCreatedAtDesc(String identifier);
    void deleteByIdentifier(String identifier);
}
