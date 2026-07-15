package com.example.domain.customers;

import com.example.sharedkernel.AggregateRoot;
import com.example.sharedkernel.Result;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer extends AggregateRoot {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Instant registeredAt;

    protected Customer() {
        // JPA only
    }

    private Customer(UUID id, String name, String email, Instant registeredAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public static Result<Customer> register(String name, String email, Instant registeredAt) {
        if (name == null || name.isBlank()) {
            return Result.failure(CustomerErrors.NAME_REQUIRED);
        }
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            return Result.failure(CustomerErrors.EMAIL_INVALID);
        }
        Customer customer = new Customer(
            UUID.randomUUID(), name.trim(), email.toLowerCase(), registeredAt);
        customer.raise(new CustomerRegisteredDomainEvent(customer.id));
        return Result.success(customer);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public Instant registeredAt() {
        return registeredAt;
    }
}
