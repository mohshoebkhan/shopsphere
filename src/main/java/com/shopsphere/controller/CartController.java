package com.shopsphere.controller;

import com.shopsphere.dto.CartRequest;
import com.shopsphere.dto.CartResponse;
import com.shopsphere.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping
    public CartResponse addToCart(@RequestBody CartRequest request) {
        return service.addToCart(request);
    }

    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable Long userId) {
        return service.getCart(userId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<?> remove(@PathVariable Long cartId) {

        service.removeItem(cartId);

        return ResponseEntity.ok(
                Map.of("message", "Item removed successfully")
        );
    }

    @PutMapping("/increase/{cartId}")
    public CartResponse increaseQuantity(@PathVariable Long cartId) {
        return service.increaseQuantity(cartId);
    }

    @PutMapping("/decrease/{cartId}")
    public CartResponse decreaseQuantity(@PathVariable Long cartId) {
        return service.decreaseQuantity(cartId);
    }

}