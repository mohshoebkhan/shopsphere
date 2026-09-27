package com.shopsphere.service;

import com.shopsphere.dto.ProductRequest;
import com.shopsphere.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse addProduct(ProductRequest request);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    List<ProductResponse> searchProducts(String productName);

    List<ProductResponse> getProductsByCategory(String category);

    List<ProductResponse> getProductsByBrand(String brand);

    List<ProductResponse> getProductsByPrice(Double min, Double max);

}