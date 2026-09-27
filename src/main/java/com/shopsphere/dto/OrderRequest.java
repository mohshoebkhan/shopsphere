package com.shopsphere.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private Long userId;

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