package com.example.application.products;

import com.example.domain.products.Product;
import java.util.Optional;
import java.util.UUID;

/**
 * Port — implemented by infrastructure. Speaks domain language only.
 */
public interface ProductRepository {

    void save(Product product);

    Optional<Product> findById(UUID id);
}
