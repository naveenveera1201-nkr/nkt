package com.nkt.api.controller;

import com.nkt.api.dto.response.ApiResponse;
import com.nkt.api.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    /** API 30 – Get Wishlist Items */
    @GetMapping
    public ResponseEntity<ApiResponse<Object>> getWishlist(@AuthenticationPrincipal String userId) {
        var items = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(ApiResponse.success(items, "ACTIVE"));
    }

    /** API 31 – Add Item to Wishlist */
    @PostMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Object>> addToWishlist(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId) {
        var item = wishlistService.addToWishlist(userId, itemId);
        return ResponseEntity.ok(ApiResponse.created(item.getId(), item));
    }

    /** API 32 – Remove Item from Wishlist */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Object>> removeFromWishlist(
            @AuthenticationPrincipal String userId,
            @PathVariable String itemId) {
        var result = wishlistService.removeFromWishlist(userId, itemId);
        return ResponseEntity.ok(ApiResponse.success(result, "DELETED"));
    }
}
