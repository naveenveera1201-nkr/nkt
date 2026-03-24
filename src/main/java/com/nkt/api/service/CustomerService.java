package com.nkt.api.service;

import com.nkt.api.dto.request.AddAddressRequest;
import com.nkt.api.dto.request.UpdateProfileRequest;
import com.nkt.api.exception.ResourceNotFoundException;
import com.nkt.api.model.Address;
import com.nkt.api.model.Store;
import com.nkt.api.model.User;
import com.nkt.api.repository.StoreRepository;
import com.nkt.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // API 7 – Get Customer Profile
    public User getProfile(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    // API 8 – Update Customer Profile
    public User updateProfile(String userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        if (req.getName()  != null) user.setName(req.getName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // API 9 – Add Delivery Address
    public Address addAddress(String userId, AddAddressRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Address address = Address.builder()
                .id(UUID.randomUUID().toString())
                .label(req.getLabel()).line1(req.getLine1()).line2(req.getLine2())
                .city(req.getCity()).state(req.getState()).pincode(req.getPincode())
                .latitude(req.getLatitude()).longitude(req.getLongitude())
                .status("ACTIVE")
                .build();
        user.getAddresses().add(address);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return address;
    }

    // API 10 – Delete Saved Address
    public void deleteAddress(String userId, String addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        boolean removed = user.getAddresses().removeIf(a -> addressId.equals(a.getId()));
        if (!removed) throw new ResourceNotFoundException("Address not found");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // API 11 – Toggle Favourite Store
    public Map<String, Object> toggleFavourite(String userId, String storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<String> favs = user.getFavouriteStoreIds();
        boolean saved;
        if (favs.contains(storeId)) { favs.remove(storeId); saved = false; }
        else                         { favs.add(storeId);    saved = true;  }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return Map.of("saved", saved, "storeId", storeId);
    }
}
