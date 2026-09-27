package com.shopsphere.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {

    private Long orderId;

    private Double totalAmount;

    private String status;

    private LocalDateTime orderDate;

    // =========================
    // DELIVERY DETAILS
    // =========================

    private String fullName;

    private String mobile;

    private String address;

    private String city;

    private String state;

    private String pincode;

    // =========================
    // PAYMENT
    // =========================

    private String paymentMethod;
}

