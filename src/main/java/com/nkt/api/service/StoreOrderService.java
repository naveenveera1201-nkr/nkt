package com.nkt.api.service;

import com.nkt.api.dto.request.*;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Order;
import com.nkt.api.repository.OrderRepository;
import com.nkt.api.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StoreOrderService {

    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    private String resolveStoreId(String ownerId) {
        return storeRepository.findAll().stream()
                .filter(s -> ownerId.equals(s.getOwnerId()))
                .map(s -> s.getId())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Store not found for owner"));
    }

    private Order getStoreOrder(String orderId, String storeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (!order.getStoreId().equals(storeId)) throw new RuntimeException("Unauthorized");
        return order;
    }

    // API 47 – Get All Store Orders
    public List<Order> getAllOrders(String ownerId, String status, int page, int limit) {
        String storeId = resolveStoreId(ownerId);
        PageRequest pr = PageRequest.of(page, limit);
        if (status != null)
            return orderRepository.findByStoreIdAndStatusOrderByCreatedAtDesc(storeId, status, pr);
        return orderRepository.findByStoreIdOrderByCreatedAtDesc(storeId, pr);
    }

    // API 48 – Accept Order
    public Order acceptOrder(String ownerId, String orderId, AcceptOrderRequest req) {
        String storeId = resolveStoreId(ownerId);
        Order order = getStoreOrder(orderId, storeId);
        if (!"placed".equals(order.getStatus()))
            throw new RuntimeException("Order cannot be accepted at status: " + order.getStatus());
        order.setStatus("accepted");
        order.setCurrentStatus("accepted");
        order.setStoreNote(req.getStoreNote());
        if (Boolean.TRUE.equals(req.getPartial()) && req.getFulfilledItems() != null)
            order.setStoreNote("Partial fulfilment. " + order.getStoreNote());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // API 49 – Reject Order
    public Order rejectOrder(String ownerId, String orderId, RejectOrderRequest req) {
        String storeId = resolveStoreId(ownerId);
        Order order = getStoreOrder(orderId, storeId);
        if (!"placed".equals(order.getStatus()))
            throw new RuntimeException("Order cannot be rejected at status: " + order.getStatus());
        order.setStatus("cancelled");
        order.setCurrentStatus("cancelled");
        order.setStoreNote(req.getReason());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // API 50 – Dispatch Order
    public Order dispatchOrder(String ownerId, String orderId, DispatchOrderRequest req) {
        String storeId = resolveStoreId(ownerId);
        Order order = getStoreOrder(orderId, storeId);
        if (!"accepted".equals(order.getStatus()))
            throw new RuntimeException("Order must be accepted before dispatch");
        Order.DeliveryAgent agent = Order.DeliveryAgent.builder()
                .name(req.getAgentName()).phone(req.getAgentPhone())
                .vehiclePlate(req.getVehiclePlate()).build();
        order.setDeliveryAgent(agent);
        order.setStatus("dispatched");
        order.setCurrentStatus("dispatched");
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    // API 51 – Deliver Order
    public Order deliverOrder(String ownerId, String orderId, DeliverOrderRequest req) {
        String storeId = resolveStoreId(ownerId);
        Order order = getStoreOrder(orderId, storeId);
        if (!"dispatched".equals(order.getStatus()))
            throw new RuntimeException("Order must be dispatched before marking as delivered");
        order.setStatus("delivered");
        order.setCurrentStatus("delivered");
        order.setDeliveryProof(req.getDeliveryProof());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
