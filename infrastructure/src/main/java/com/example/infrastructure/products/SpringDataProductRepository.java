package com.example.infrastructure.products;

import com.example.domain.products.Product;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataProductRepository extends JpaRepository<Product, UUID> {
}
