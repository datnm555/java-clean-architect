package com.example.application.products;

import com.example.domain.products.Product;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductResponse(UUID id, String name, BigDecimal price, Instant createdAt) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
            product.id(), product.name(), product.price(), product.createdAt());
    }
}
