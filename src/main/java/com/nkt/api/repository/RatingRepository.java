package com.nkt.api.repository;

import com.nkt.api.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RatingRepository extends MongoRepository<Rating, String> {
    List<Rating> findByStoreId(String storeId);
    boolean existsByOrderIdAndCustomerId(String orderId, String customerId);
}
