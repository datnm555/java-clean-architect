package com.example.application.customers;

import com.example.domain.customers.Customer;
import java.util.Optional;
import java.util.UUID;

/**
 * Port — implemented by infrastructure. Speaks domain language only.
 */
public interface CustomerRepository {

    void save(Customer customer);

    Optional<Customer> findById(UUID id);

    boolean existsByEmail(String email);
}
