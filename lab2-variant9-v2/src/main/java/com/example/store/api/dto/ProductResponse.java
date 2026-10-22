package com.example.store.api.dto;

import com.example.store.domain.Product;

public record ProductResponse(int id, String name, String brand, double price, int countInStock) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getPrice(),
                product.getCountInStock()
        );
    }
}
