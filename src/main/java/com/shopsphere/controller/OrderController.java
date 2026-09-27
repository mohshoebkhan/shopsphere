package com.shopsphere.controller;

import com.shopsphere.dto.OrderItemResponse;
import com.shopsphere.dto.OrderRequest;
import com.shopsphere.dto.OrderResponse;
import com.shopsphere.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest request) {
        return service.placeOrder(request);
    }

    @GetMapping("/{userId}")
    public List<OrderResponse> getOrders(@PathVariable Long userId) {
        return service.getOrders(userId);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponse> getOrderItems(
            @PathVariable Long orderId) {

        return service.getOrderItems(orderId);
    }

    @PutMapping("/{orderId}/cancel")
    public String cancelOrder(
            @PathVariable Long orderId,
            @RequestParam Long userId) {

        service.cancelOrder(orderId, userId);

        return "Order cancelled successfully";
    }
}