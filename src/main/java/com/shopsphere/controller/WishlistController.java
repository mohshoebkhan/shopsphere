package com.shopsphere.controller;

import com.shopsphere.dto.WishlistRequest;
import com.shopsphere.dto.WishlistResponse;
import com.shopsphere.service.WishlistService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "http://localhost:4300")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) {
        this.service = service;
    }

    @PostMapping
    public WishlistResponse addToWishlist(
            @RequestBody WishlistRequest request) {

        return service.addToWishlist(request);
    }

    @GetMapping("/{userId}")
    public List<WishlistResponse> getWishlist(
            @PathVariable Long userId) {

        return service.getWishlist(userId);
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {

        service.removeFromWishlist(
                userId,
                productId
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Removed from wishlist"
                )
        );
    }
}