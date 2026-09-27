package com.shopsphere.service;

import com.shopsphere.dto.OrderItemResponse;
import com.shopsphere.dto.OrderRequest;
import com.shopsphere.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);
    List<OrderResponse> getOrders(Long userId);
    List<OrderItemResponse> getOrderItems(Long orderId);
    void cancelOrder(Long orderId, Long userId);

}