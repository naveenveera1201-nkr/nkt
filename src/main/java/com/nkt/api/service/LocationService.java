package com.nkt.api.service;

import com.nkt.api.model.Store;
import com.nkt.api.model.StockItem;
import com.nkt.api.repository.StoreRepository;
import com.nkt.api.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final StoreRepository    storeRepository;
    private final StockItemRepository stockItemRepository;

    // API 34 – Reverse Geocode Location (stub — real impl calls Maps API)
    public Map<String, Object> reverseGeocode(double latitude, double longitude) {
        return Map.of(
                "latitude", latitude, "longitude", longitude,
                "address", "Address lookup requires Google Maps / MapMyIndia API integration",
                "city", "Unknown", "state", "Unknown", "pincode", "000000"
        );
    }

    // API 35 – Global Search
    public Map<String, Object> globalSearch(String q, Double latitude, Double longitude, String categoryId) {
        String query = q.toLowerCase();

        List<Store> stores = storeRepository.findByStatus("ACTIVE").stream()
                .filter(s -> s.getName() != null && s.getName().toLowerCase().contains(query))
                .filter(s -> categoryId == null || categoryId.equals(s.getCategoryId()))
                .collect(Collectors.toList());

        List<StockItem> items = stockItemRepository.findByStoreIdAndStatus("", "ACTIVE").stream()
                .filter(i -> i.getName() != null && i.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());

        return Map.of("query", q, "stores", stores, "items", items,
                      "totalResults", stores.size() + items.size());
    }
}
