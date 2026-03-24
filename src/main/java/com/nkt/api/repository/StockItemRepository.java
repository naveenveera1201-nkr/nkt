package com.nkt.api.repository;

import com.nkt.api.model.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface StockItemRepository extends MongoRepository<StockItem, String> {
    List<StockItem> findByStoreIdAndStatus(String storeId, String status);
    List<StockItem> findByStoreIdAndCategoryAndStatus(String storeId, String category, String status);
    List<StockItem> findByStoreIdAndAvailableAndStatus(String storeId, boolean available, String status);
    List<StockItem> findDistinctCategoryByStoreIdAndStatus(String storeId, String status);
}
