package com.nkt.api.repository;

import com.nkt.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIdentifier(String identifier);
    boolean existsByIdentifier(String identifier);
}
