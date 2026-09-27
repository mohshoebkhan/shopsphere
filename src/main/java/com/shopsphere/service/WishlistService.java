package com.shopsphere.service;

import com.shopsphere.dto.WishlistRequest;
import com.shopsphere.dto.WishlistResponse;

import java.util.List;

public interface WishlistService {

    WishlistResponse addToWishlist(WishlistRequest request);

    List<WishlistResponse> getWishlist(Long userId);

    void removeFromWishlist(Long userId, Long productId);
}