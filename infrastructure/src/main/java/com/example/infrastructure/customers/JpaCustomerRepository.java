package com.example.infrastructure.customers;

import com.example.application.customers.CustomerRepository;
import com.example.domain.customers.Customer;
import com.example.infrastructure.events.DomainEvents;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
class JpaCustomerRepository implements CustomerRepository {

    private final SpringDataCustomerRepository jpa;
    private final DomainEvents domainEvents;

    JpaCustomerRepository(SpringDataCustomerRepository jpa, DomainEvents domainEvents) {
        this.jpa = jpa;
        this.domainEvents = domainEvents;
    }

    @Override
    public void save(Customer customer) {
        jpa.save(customer);
        domainEvents.publishFrom(customer);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpa.existsByEmail(email);
    }
}
