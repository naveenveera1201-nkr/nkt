package com.nkt.api.repository;

import com.nkt.api.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(String customerId, Pageable pageable);
    List<Order> findByCustomerIdAndStatusOrderByCreatedAtDesc(String customerId, String status, Pageable pageable);
    List<Order> findByStoreIdOrderByCreatedAtDesc(String storeId, Pageable pageable);
    List<Order> findByStoreIdAndStatusOrderByCreatedAtDesc(String storeId, String status, Pageable pageable);
}
