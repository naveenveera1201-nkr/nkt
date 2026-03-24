package com.nkt.api.repository;

import com.nkt.api.model.Store;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface StoreRepository extends MongoRepository<Store, String> {
    List<Store> findByCategoryIdAndStatus(String categoryId, String status);
    List<Store> findByStatus(String status);
}
