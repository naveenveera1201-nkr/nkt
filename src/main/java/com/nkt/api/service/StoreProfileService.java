package com.nkt.api.service;

import com.nkt.api.dto.request.ToggleStoreStatusRequest;
import com.nkt.api.dto.request.UpdateStoreProfileRequest;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Order;
import com.nkt.api.model.Store;
import com.nkt.api.repository.OrderRepository;
import com.nkt.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StoreProfileService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    private Store getStoreByOwner(String ownerId) {
        return storeRepository.findAll().stream()
                .filter(s -> ownerId.equals(s.getOwnerId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Store not found for owner: " + ownerId));
    }

    // API 36 – Get Store Profile
    public Store getProfile(String ownerId) {
        return getStoreByOwner(ownerId);
    }

    // API 37 – Update Store Profile
    public Store updateProfile(String ownerId, UpdateStoreProfileRequest req) {
        Store store = getStoreByOwner(ownerId);
        if (req.getName()  != null) store.setName(req.getName());
        if (req.getPhone() != null) store.setPhone(req.getPhone());
        if (req.getHours() != null) store.setHours(req.getHours());
        store.setUpdatedAt(LocalDateTime.now());
        return storeRepository.save(store);
    }

    // API 38 – Toggle Store Open/Closed
    public Map<String, Object> toggleStatus(String ownerId, ToggleStoreStatusRequest req) {
        Store store = getStoreByOwner(ownerId);
        store.setOpen(req.getIsOpen());
        store.setUpdatedAt(LocalDateTime.now());
        storeRepository.save(store);
        return Map.of("storeId", store.getId(), "isOpen", store.isOpen(),
                      "updatedAt", LocalDateTime.now().toString());
    }

    // API 39 – Get Dashboard Stats
    public Map<String, Object> getDashboard(String ownerId) {
        Store store = getStoreByOwner(ownerId);
        List<Order> allOrders = orderRepository.findByStoreIdOrderByCreatedAtDesc(
                store.getId(), PageRequest.of(0, 1000));

        LocalDate today = LocalDate.now();
        long todayCount  = allOrders.stream().filter(o -> o.getCreatedAt() != null
                && o.getCreatedAt().toLocalDate().equals(today)).count();
        long newOrders   = allOrders.stream().filter(o -> "placed".equals(o.getStatus())).count();
        double todayRev  = allOrders.stream().filter(o -> o.getCreatedAt() != null
                && o.getCreatedAt().toLocalDate().equals(today)
                && "delivered".equals(o.getStatus())).mapToDouble(Order::getTotalAmount).sum();

        Map<String, Object> dash = new HashMap<>();
        dash.put("storeId",    store.getId());
        dash.put("newOrders",  newOrders);
        dash.put("todayOrders",todayCount);
        dash.put("todayRevenue", todayRev);
        dash.put("currentStatus", store.isOpen() ? "OPEN" : "CLOSED");
        return dash;
    }
}
