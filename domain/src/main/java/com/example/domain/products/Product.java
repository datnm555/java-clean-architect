package com.example.domain.products;

import com.example.sharedkernel.AggregateRoot;
import com.example.sharedkernel.Result;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product extends AggregateRoot {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Instant createdAt;

    protected Product() {
        // JPA only
    }

    private Product(UUID id, String name, BigDecimal price, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = createdAt;
    }

    public static Result<Product> create(String name, BigDecimal price, Instant createdAt) {
        if (name == null || name.isBlank()) {
            return Result.failure(ProductErrors.NAME_REQUIRED);
        }
        if (price == null || price.signum() <= 0) {
            return Result.failure(ProductErrors.PRICE_INVALID);
        }
        Product product = new Product(UUID.randomUUID(), name.trim(), price, createdAt);
        product.raise(new ProductCreatedDomainEvent(product.id));
        return Result.success(product);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public Instant createdAt() {
        return createdAt;
    }
}
