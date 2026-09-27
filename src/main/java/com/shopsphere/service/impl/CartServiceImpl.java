package com.shopsphere.service.impl;

import com.shopsphere.dto.CartRequest;
import com.shopsphere.dto.CartResponse;
import com.shopsphere.entity.Cart;
import com.shopsphere.entity.Product;
import com.shopsphere.repository.CartRepository;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository repository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository repository,
                           ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

    @Override
    public CartResponse addToCart(CartRequest request) {

        Cart cart = repository.findByUserIdAndProductId(
                request.getUserId(),
                request.getProductId()
        ).orElse(null);

        if (cart == null) {

            cart = Cart.builder()
                    .userId(request.getUserId())
                    .productId(request.getProductId())
                    .quantity(request.getQuantity())
                    .build();

        } else {

            cart.setQuantity(cart.getQuantity() + request.getQuantity());

        }

        repository.save(cart);

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .productName(product.getProductName())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(product.getPrice() * cart.getQuantity())
                .build();
    }

    @Override
    public List<CartResponse> getCart(Long userId) {

        return repository.findByUserId(userId)
                .stream()
                .map(cart -> {

                    Product product = productRepository.findById(cart.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    return CartResponse.builder()
                            .cartId(cart.getId())
                            .userId(cart.getUserId())
                            .productId(cart.getProductId())
                            .productName(product.getProductName())
                            .imageUrl(product.getImageUrl())
                            .price(product.getPrice())
                            .quantity(cart.getQuantity())
                            .totalPrice(product.getPrice() * cart.getQuantity())
                            .build();

                })
                .toList();
    }

    @Override
    public void removeItem(Long cartId) {

        repository.deleteById(cartId);

    }

    @Override
    public CartResponse increaseQuantity(Long cartId) {

        Cart cart = repository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.setQuantity(cart.getQuantity() + 1);

        repository.save(cart);

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .productName(product.getProductName())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(product.getPrice() * cart.getQuantity())
                .build();
    }

    @Override
    public CartResponse decreaseQuantity(Long cartId) {

        Cart cart = repository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getQuantity() > 1) {
            cart.setQuantity(cart.getQuantity() - 1);
            repository.save(cart);
        }

        Product product = productRepository.findById(cart.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return CartResponse.builder()
                .cartId(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .productName(product.getProductName())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .quantity(cart.getQuantity())
                .totalPrice(product.getPrice() * cart.getQuantity())
                .build();
    }
}