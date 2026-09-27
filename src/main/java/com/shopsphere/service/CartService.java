package com.shopsphere.service;

import com.shopsphere.dto.CartRequest;
import com.shopsphere.dto.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    List<CartResponse> getCart(Long userId);

    void removeItem(Long cartId);

    CartResponse increaseQuantity(Long cartId);

    CartResponse decreaseQuantity(Long cartId);

}