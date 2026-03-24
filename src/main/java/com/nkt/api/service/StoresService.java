package com.nkt.api.service;

import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Store;
import com.nkt.api.model.StockItem;
import com.nkt.api.repository.StockItemRepository;
import com.nkt.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoresService {

    private final StoreRepository    storeRepository;
    private final StockItemRepository stockItemRepository;

    // API 15 – List Stores by Category
    public List<Store> listByCategory(String categoryId) {
        return storeRepository.findByCategoryIdAndStatus(categoryId, "ACTIVE");
    }

    // API 16 – Get Store Details
    public Store getStoreDetails(String storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found: " + storeId));
    }

    // API 17 – Get Store Product Catalogue
    public List<StockItem> getProducts(String storeId, String categoryFilter, Boolean available) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found: " + storeId));
        if (categoryFilter != null)
            return stockItemRepository.findByStoreIdAndCategoryAndStatus(storeId, categoryFilter, "ACTIVE");
        if (available != null)
            return stockItemRepository.findByStoreIdAndAvailableAndStatus(storeId, available, "ACTIVE");
        return stockItemRepository.findByStoreIdAndStatus(storeId, "ACTIVE");
    }

    // API 18 – Get Store Services (reuses stock items of type "service")
    public List<StockItem> getServices(String storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found: " + storeId));
        return stockItemRepository.findByStoreIdAndCategoryAndStatus(storeId, "service", "ACTIVE");
    }

    // API 19 – Get Available Time Slots (stub — real impl integrates calendar)
    public Object getAvailability(String storeId, String serviceId, String fromDate, String toDate) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found: " + storeId));
        return java.util.Map.of(
                "storeId", storeId, "serviceId", serviceId,
                "fromDate", fromDate, "slots", java.util.List.of());
    }
}
