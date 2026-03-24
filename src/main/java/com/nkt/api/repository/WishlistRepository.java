package com.nkt.api.repository;

import com.nkt.api.model.WishlistItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends MongoRepository<WishlistItem, String> {
    List<WishlistItem> findByCustomerIdAndStatus(String customerId, String status);
    Optional<WishlistItem> findByCustomerIdAndItemId(String customerId, String itemId);
}
