package com.shopsphere.service.impl;

import com.shopsphere.dto.ProductRequest;
import com.shopsphere.dto.ProductResponse;
import com.shopsphere.entity.Product;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest request) {

        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .brand(request.getBrand())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .build();

        Product savedProduct = repository.save(product);

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .productName(savedProduct.getProductName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .quantity(savedProduct.getQuantity())
                .brand(savedProduct.getBrand())
                .category(savedProduct.getCategory())
                .imageUrl(savedProduct.getImageUrl())
                .build();
    }

    @Override
    public List<ProductResponse> getAllProducts() {

        return repository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }



    @Override
    public ProductResponse getProductById(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .brand(product.getBrand())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .build();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        Product updated = repository.save(product);

        return ProductResponse.builder()
                .id(updated.getId())
                .productName(updated.getProductName())
                .description(updated.getDescription())
                .price(updated.getPrice())
                .quantity(updated.getQuantity())
                .brand(updated.getBrand())
                .category(updated.getCategory())
                .imageUrl(updated.getImageUrl())
                .build();
    }
    @Override
    public void deleteProduct(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        repository.delete(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String productName) {

        String keyword = productName.trim();

        return repository
                .findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrBrandContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        keyword
                )
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(String category) {

        return repository.findByCategoryIgnoreCase(category)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByBrand(String brand) {

        return repository.findByBrandIgnoreCase(brand)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByPrice(Double min, Double max) {

        return repository.findByPriceBetween(min, max)
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .quantity(product.getQuantity())
                        .brand(product.getBrand())
                        .category(product.getCategory())
                        .imageUrl(product.getImageUrl())
                        .build())
                .toList();
    }


}
