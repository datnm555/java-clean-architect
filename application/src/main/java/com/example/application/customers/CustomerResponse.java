package com.example.application.customers;

import com.example.domain.customers.Customer;
import java.time.Instant;
import java.util.UUID;

public record CustomerResponse(UUID id, String name, String email, Instant registeredAt) {

    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
            customer.id(), customer.name(), customer.email(), customer.registeredAt());
    }
}
