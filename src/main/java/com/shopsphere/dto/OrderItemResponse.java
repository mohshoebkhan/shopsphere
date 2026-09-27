package com.shopsphere.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long productId;

    private String productName;

    private String description;

    private String brand;

    private String category;

    private String imageUrl;

    private Integer quantity;

    private Double price;

    private Double totalPrice;
}