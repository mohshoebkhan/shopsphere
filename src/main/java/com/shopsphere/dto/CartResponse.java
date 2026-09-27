package com.shopsphere.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {

    private Long cartId;

    private Long userId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double price;

    private Integer quantity;

    private Double totalPrice;
}