package com.nkt.api.service;

import com.nkt.api.dto.request.*;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.StockItem;
import com.nkt.api.repository.StockItemRepository;
import com.nkt.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockItemRepository stockItemRepository;
    private final StoreRepository     storeRepository;

    private String resolveStoreId(String ownerId) {
        return storeRepository.findAll().stream()
                .filter(s -> ownerId.equals(s.getOwnerId()))
                .map(s -> s.getId())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Store not found for owner"));
    }

    // API 40 – Get Full Stock List
    public List<StockItem> getStockList(String ownerId, String category, Boolean available) {
        String storeId = resolveStoreId(ownerId);
        if (category  != null) return stockItemRepository.findByStoreIdAndCategoryAndStatus(storeId, category, "ACTIVE");
        if (available != null) return stockItemRepository.findByStoreIdAndAvailableAndStatus(storeId, available, "ACTIVE");
        return stockItemRepository.findByStoreIdAndStatus(storeId, "ACTIVE");
    }

    // API 41 – Add New Stock Item
    public StockItem addItem(String ownerId, AddStockItemRequest req) {
        String storeId = resolveStoreId(ownerId);
        StockItem item = StockItem.builder()
                .storeId(storeId).name(req.getName()).sub(req.getSub())
                .price(req.getPrice()).emoji(req.getEmoji())
                .category(req.getCategory()).qty(req.getQty())
                .available(req.getQty() > 0).custom(true)
                .status("ACTIVE").createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).createdBy(ownerId).build();
        return stockItemRepository.save(item);
    }

    // API 42 – List Stock Sub-categories
    public List<String> listCategories(String ownerId) {
        String storeId = resolveStoreId(ownerId);
        return stockItemRepository.findByStoreIdAndStatus(storeId, "ACTIVE").stream()
                .map(StockItem::getCategory).distinct().sorted().collect(Collectors.toList());
    }

    // API 43 – Update Stock Item (Full Update)
    public StockItem updateItem(String ownerId, String itemId, UpdateStockItemRequest req) {
        StockItem item = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
        if (req.getName()      != null) item.setName(req.getName());
        if (req.getPrice()     != null) item.setPrice(req.getPrice());
        if (req.getQty()       != null) item.setQty(req.getQty());
        if (req.getAvailable() != null) item.setAvailable(req.getAvailable());
        if (req.getSub()       != null) item.setSub(req.getSub());
        if (req.getEmoji()     != null) item.setEmoji(req.getEmoji());
        item.setUpdatedAt(LocalDateTime.now());
        return stockItemRepository.save(item);
    }

    // API 44 – Adjust Stock Quantity (Delta)
    public StockItem adjustQty(String ownerId, String itemId, AdjustStockRequest req) {
        StockItem item = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
        int newQty = item.getQty() + req.getDelta();
        if (newQty < 0) throw new RuntimeException("Stock cannot go below 0");
        item.setQty(newQty);
        item.setAvailable(newQty > 0);
        item.setUpdatedAt(LocalDateTime.now());
        return stockItemRepository.save(item);
    }

    // API 45 – Toggle Item Availability
    public StockItem toggleAvailability(String ownerId, String itemId, ToggleAvailabilityRequest req) {
        StockItem item = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
        item.setAvailable(req.getAvailable());
        item.setUpdatedAt(LocalDateTime.now());
        return stockItemRepository.save(item);
    }

    // API 46 – Remove Custom Stock Item
    public Map<String, String> deleteItem(String ownerId, String itemId) {
        StockItem item = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
        if (!item.isCustom())
            throw new RuntimeException("Only custom items can be deleted");
        item.setStatus("DELETED");
        item.setUpdatedAt(LocalDateTime.now());
        stockItemRepository.save(item);
        return Map.of("message", "Item removed from catalogue");
    }
}
