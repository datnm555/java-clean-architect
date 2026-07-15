package com.example.application.customers;

import com.example.domain.customers.Customer;
import com.example.domain.customers.CustomerErrors;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.Result;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterCustomerUseCase {

    private final CustomerRepository customers;
    private final DateTimeProvider clock;

    RegisterCustomerUseCase(CustomerRepository customers, DateTimeProvider clock) {
        this.customers = customers;
        this.clock = clock;
    }

    @Transactional
    public Result<UUID> handle(RegisterCustomerCommand command) {
        if (customers.existsByEmail(command.email().toLowerCase())) {
            return Result.failure(CustomerErrors.EMAIL_TAKEN);
        }
        return Customer.register(command.name(), command.email(), clock.now())
            .map(customer -> {
                customers.save(customer);
                return customer.id();
            });
    }
}
