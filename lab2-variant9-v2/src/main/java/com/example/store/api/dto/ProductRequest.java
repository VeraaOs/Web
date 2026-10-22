package com.example.store.api.dto;

import com.example.store.domain.Product;

public record ProductRequest(String name, String brand, double price, int countInStock) {

    public Product toProduct() {
        Product product = new Product();
        product.setName(name);
        product.setBrand(brand);
        product.setPrice(price);
        product.setCountInStock(countInStock);
        return product;
    }
}
