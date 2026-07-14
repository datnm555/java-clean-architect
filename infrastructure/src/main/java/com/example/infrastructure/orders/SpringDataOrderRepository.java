package com.example.infrastructure.orders;

import com.example.domain.orders.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataOrderRepository extends JpaRepository<Order, UUID> {
}
