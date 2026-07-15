package com.example.infrastructure.customers;

import com.example.domain.customers.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataCustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByEmail(String email);
}
