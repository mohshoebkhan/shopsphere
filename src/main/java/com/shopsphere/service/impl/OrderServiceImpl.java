package com.shopsphere.service.impl;

import com.shopsphere.dto.OrderItemResponse;
import com.shopsphere.dto.OrderRequest;
import com.shopsphere.dto.OrderResponse;
import com.shopsphere.entity.Cart;
import com.shopsphere.entity.Order;
import com.shopsphere.entity.OrderItem;
import com.shopsphere.entity.Product;
import com.shopsphere.repository.CartRepository;
import com.shopsphere.repository.OrderItemRepository;
import com.shopsphere.repository.OrderRepository;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        List<Cart> cartItems = cartRepository.findByUserId(request.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = 0;

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            total += product.getPrice() * cart.getQuantity();
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(total)
                .status("PLACED")
                .orderDate(LocalDateTime.now())

                // Delivery details
                .fullName(request.getFullName())
                .mobile(request.getMobile())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())

                // Payment
                .paymentMethod(request.getPaymentMethod())
                .build();

        orderRepository.save(order);

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = OrderItem.builder()
                    .orderId(order.getId())
                    .productId(product.getId())
                    .quantity(cart.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItemRepository.save(item);
        }

        cartRepository.deleteAll(cartItems);

        return OrderResponse.builder()
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())

                // Delivery details
                .fullName(order.getFullName())
                .mobile(order.getMobile())
                .address(order.getAddress())
                .city(order.getCity())
                .state(order.getState())
                .pincode(order.getPincode())

                // Payment
                .paymentMethod(order.getPaymentMethod())
                .build();
    }

    @Override
    public List<OrderResponse> getOrders(Long userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .orderDate(order.getOrderDate())

                        // Delivery details
                        .fullName(order.getFullName())
                        .mobile(order.getMobile())
                        .address(order.getAddress())
                        .city(order.getCity())
                        .state(order.getState())
                        .pincode(order.getPincode())

                        // Payment
                        .paymentMethod(order.getPaymentMethod())

                        .build())
                .toList();

    }

    @Override
    public List<OrderItemResponse> getOrderItems(Long orderId) {

        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(item -> {

                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() ->
                                    new RuntimeException("Product not found"));

                    return OrderItemResponse.builder()
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .description(product.getDescription())
                            .brand(product.getBrand())
                            .category(product.getCategory())
                            .imageUrl(product.getImageUrl())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .totalPrice(item.getPrice() * item.getQuantity())
                            .build();
                })
                .toList();
    }

    @Override
    public void cancelOrder(Long orderId, Long userId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You are not authorized to cancel this order");
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException(
                    "Order is already cancelled");
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException(
                    "Delivered order cannot be cancelled");
        }

        order.setStatus("CANCELLED");

        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        String newStatus = status.trim().toUpperCase();

        switch (newStatus) {

            case "PLACED":
            case "CONFIRMED":
            case "PROCESSING":
            case "SHIPPED":
            case "DELIVERED":
            case "CANCELLED":
                break;

            default:
                throw new RuntimeException(
                        "Invalid order status: " + status
                );
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException(
                    "Cancelled order status cannot be changed"
            );
        }

        if ("DELIVERED".equalsIgnoreCase(order.getStatus())
                && !"DELIVERED".equals(newStatus)) {

            throw new RuntimeException(
                    "Delivered order status cannot be changed"
            );
        }

        order.setStatus(newStatus);

        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .orderDate(order.getOrderDate())
                        .fullName(order.getFullName())
                        .mobile(order.getMobile())
                        .address(order.getAddress())
                        .city(order.getCity())
                        .state(order.getState())
                        .pincode(order.getPincode())
                        .paymentMethod(order.getPaymentMethod())
                        .build())
                .toList();
    }
}