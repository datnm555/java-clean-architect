package com.example.infrastructure.orders;

import com.example.application.orders.OrderRepository;
import com.example.domain.orders.Order;
import com.example.infrastructure.events.DomainEvents;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaOrderRepository implements OrderRepository {

    private final SpringDataOrderRepository jpa;
    private final DomainEvents domainEvents;

    JpaOrderRepository(SpringDataOrderRepository jpa, DomainEvents domainEvents) {
        this.jpa = jpa;
        this.domainEvents = domainEvents;
    }

    @Override
    public void save(Order order) {
        jpa.save(order);
        domainEvents.publishFrom(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return jpa.findById(id);
    }
}
