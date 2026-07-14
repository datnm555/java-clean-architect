package com.example.infrastructure.products;

import com.example.application.products.ProductRepository;
import com.example.domain.products.Product;
import com.example.infrastructure.events.DomainEvents;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaProductRepository implements ProductRepository {

    private final SpringDataProductRepository jpa;
    private final DomainEvents domainEvents;

    JpaProductRepository(SpringDataProductRepository jpa, DomainEvents domainEvents) {
        this.jpa = jpa;
        this.domainEvents = domainEvents;
    }

    @Override
    public void save(Product product) {
        jpa.save(product);
        domainEvents.publishFrom(product);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpa.findById(id);
    }
}
