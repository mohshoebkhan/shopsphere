package com.shopsphere.service.impl;

import com.shopsphere.dto.WishlistRequest;
import com.shopsphere.dto.WishlistResponse;
import com.shopsphere.entity.Product;
import com.shopsphere.entity.Wishlist;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.repository.WishlistRepository;
import com.shopsphere.service.WishlistService;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Override
    public WishlistResponse addToWishlist(
            WishlistRequest request) {

        Wishlist existing =
                wishlistRepository
                        .findByUserIdAndProductId(
                                request.getUserId(),
                                request.getProductId()
                        )
                        .orElse(null);

        if (existing != null) {

            return mapToResponse(existing);
        }

        Wishlist wishlist = Wishlist.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .build();

        Wishlist saved =
                wishlistRepository.save(wishlist);

        return mapToResponse(saved);
    }

    @Override
    public List<WishlistResponse> getWishlist(
            Long userId) {

        return wishlistRepository
                .findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {

        wishlistRepository.deleteByUserIdAndProductId(
                userId,
                productId
        );
    }

    private WishlistResponse mapToResponse(
            Wishlist wishlist) {

        Product product =
                productRepository
                        .findById(wishlist.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"
                                )
                        );

        return WishlistResponse.builder()
                .wishlistId(wishlist.getId())
                .userId(wishlist.getUserId())
                .productId(wishlist.getProductId())
                .productName(product.getProductName())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .build();
    }
}