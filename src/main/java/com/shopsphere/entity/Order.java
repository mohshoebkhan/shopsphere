package com.shopsphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double totalAmount;

    private String status;

    private LocalDateTime orderDate;

    // =========================
    // DELIVERY DETAILS
    // =========================

    private String fullName;

    private String mobile;

    @Column(length = 1000)
    private String address;

    private String city;

    private String state;

    private String pincode;

    // =========================
    // PAYMENT
    // =========================

    private String paymentMethod;
}