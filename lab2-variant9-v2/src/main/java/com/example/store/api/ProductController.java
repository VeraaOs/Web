package com.example.store.api;

import com.example.store.application.ProductService;
import com.example.store.api.dto.ProductRequest;
import com.example.store.api.dto.ProductResponse;
import com.example.store.api.dto.PurchaseRequest;
import com.example.store.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "CRUD магазина электроники. Хранилище в памяти, лимит 50 товаров.")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Получить список всех товаров")
    public List<ProductResponse> getAll() {
        return service.getAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по id")
    public ProductResponse getById(@PathVariable int id) {
        return ProductResponse.from(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Добавить товар",
            description = "Возвращает 201 Created. Лимит магазина — 50 товаров.")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest request) {
        Product created = service.create(request.toProduct());
        return ResponseEntity
                .created(URI.create("/api/products/" + created.getId()))
                .body(ProductResponse.from(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар по id",
            description = "Полностью заменяет name, brand, price, countInStock.")
    public ProductResponse update(@PathVariable int id, @RequestBody ProductRequest request) {
        return ProductResponse.from(service.update(id, request.toProduct()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар по id",
            description = "Возвращает 204 No Content.")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/purchase")
    @Operation(summary = "Купить товар",
            description = "Уменьшает countInStock. Нельзя купить больше, чем есть на складе.")
    public ProductResponse buy(@PathVariable int id, @RequestBody PurchaseRequest request) {
        return ProductResponse.from(service.buy(id, request.quantity()));
    }
}
