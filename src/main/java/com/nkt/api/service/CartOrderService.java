package com.nkt.api.service;

import com.nkt.api.dto.request.CancelOrderRequest;
import com.nkt.api.dto.request.PlaceOrderRequest;
import com.nkt.api.dto.request.RateOrderRequest;
import com.nkt.api.dto.request.ValidateCartRequest;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Order;
import com.nkt.api.model.Rating;
import com.nkt.api.model.StockItem;
import com.nkt.api.repository.OrderRepository;
import com.nkt.api.repository.RatingRepository;
import com.nkt.api.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartOrderService {

    private final OrderRepository     orderRepository;
    private final StockItemRepository stockItemRepository;
    private final RatingRepository    ratingRepository;

    // API 20 – Validate Cart
    public Map<String, Object> validateCart(ValidateCartRequest req) {
        List<Map<String, Object>> updated = new ArrayList<>();
        for (Map<String, Object> item : req.getItems()) {
            String itemId = (String) item.get("itemId");
            StockItem si = stockItemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + itemId));
            Map<String, Object> out = new HashMap<>(item);
            out.put("currentPrice", si.getPrice());
            out.put("available", si.isAvailable());
            updated.add(out);
        }
        return Map.of("storeId", req.getStoreId(), "items", updated, "valid", true);
    }

    // API 21 – Place New Order
    public Order placeOrder(PlaceOrderRequest req, String customerId) {
        List<Order.OrderItem> items = req.getItems().stream().map(i -> {
            StockItem si = stockItemRepository.findById(i.getItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + i.getItemId()));
            return Order.OrderItem.builder()
                    .itemId(i.getItemId()).itemName(si.getName())
                    .qty(i.getQty()).price(si.getPrice()).build();
        }).collect(Collectors.toList());

        double total = items.stream().mapToDouble(i -> i.getPrice() * i.getQty()).sum();

        Order order = Order.builder()
                .customerId(customerId).storeId(req.getStoreId())
                .categoryId(req.getCategoryId()).addressId(req.getAddressId())
                .paymentMethod(req.getPaymentMethod()).appointmentSlot(req.getAppointmentSlot())
                .notes(req.getNotes()).urgency(req.getUrgency()).orderType(req.getOrderType())
                .items(items).status("placed").currentStatus("placed")
                .totalAmount(total).createdAt(LocalDateTime.now())
                .statusTimeline(List.of(Map.of("status","placed","at", LocalDateTime.now().toString())))
                .createdBy(customerId)
                .build();
        return orderRepository.save(order);
    }

    // API 22 – Get Customer Order History
    public List<Order> getOrderHistory(String customerId, String status, int page, int limit) {
        PageRequest pr = PageRequest.of(page, limit);
        if (status != null)
            return orderRepository.findByCustomerIdAndStatusOrderByCreatedAtDesc(customerId, status, pr);
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId, pr);
    }

    // API 23 – Get Order Detail
    public Order getOrderDetail(String orderId, String customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getCustomerId().equals(customerId))
            throw new RuntimeException("Unauthorized");
        return order;
    }

    // API 24 – Real-time Order Tracking (REST poll)
    public Map<String, Object> trackOrder(String orderId, String customerId) {
        Order order = getOrderDetail(orderId, customerId);
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("status", order.getStatus());
        if (order.getDeliveryAgent() != null) {
            result.put("agentLatitude",  order.getDeliveryAgent().getLatitude());
            result.put("agentLongitude", order.getDeliveryAgent().getLongitude());
        }
        return result;
    }

    // API 25 – Cancel Order
    public Order cancelOrder(String orderId, String customerId, CancelOrderRequest req) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getCustomerId().equals(customerId)) throw new RuntimeException("Unauthorized");
        if (!"placed".equals(order.getStatus()))
            throw new RuntimeException("Order cannot be cancelled at status: " + order.getStatus());
        order.setStatus("cancelled");
        order.setCurrentStatus("cancelled");
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // API 26 – Rate Order & Store
    public Rating rateOrder(String orderId, String customerId, RateOrderRequest req) {
        orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (ratingRepository.existsByOrderIdAndCustomerId(orderId, customerId))
            throw new RuntimeException("Order already rated");
        Order order = orderRepository.findById(orderId).get();
        Rating rating = Rating.builder()
                .orderId(orderId).storeId(order.getStoreId())
                .customerId(customerId).rating(req.getRating())
                .review(req.getReview()).status("ACTIVE")
                .createdAt(LocalDateTime.now()).build();
        return ratingRepository.save(rating);
    }
}
