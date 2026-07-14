package com.example.application.orders;

import com.example.domain.orders.Order;
import java.util.Optional;
import java.util.UUID;

/**
 * Port — implemented by infrastructure. Speaks domain language only.
 */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(UUID id);
}
