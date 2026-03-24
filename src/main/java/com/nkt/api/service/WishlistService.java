package com.nkt.api.service;

import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.StockItem;
import com.nkt.api.model.WishlistItem;
import com.nkt.api.repository.StockItemRepository;
import com.nkt.api.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository  wishlistRepository;
    private final StockItemRepository stockItemRepository;

    // API 30 – Get Wishlist Items
    public List<WishlistItem> getWishlist(String customerId) {
        return wishlistRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");
    }

    // API 31 – Add Item to Wishlist
    public WishlistItem addToWishlist(String customerId, String itemId) {
        StockItem si = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
        return wishlistRepository.findByCustomerIdAndItemId(customerId, itemId)
                .orElseGet(() -> {
                    WishlistItem wi = WishlistItem.builder()
                            .customerId(customerId).itemId(itemId)
                            .itemName(si.getName()).price(si.getPrice())
                            .available(si.isAvailable()).status("ACTIVE")
                            .createdAt(LocalDateTime.now()).build();
                    return wishlistRepository.save(wi);
                });
    }

    // API 32 – Remove Item from Wishlist
    public Map<String, String> removeFromWishlist(String customerId, String itemId) {
        WishlistItem wi = wishlistRepository.findByCustomerIdAndItemId(customerId, itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist item not found"));
        wi.setStatus("DELETED");
        wishlistRepository.save(wi);
        return Map.of("message", "Item removed from wishlist");
    }
}
