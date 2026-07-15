package com.example.domain.customers;

import com.example.sharedkernel.Error;
import java.util.UUID;

public final class CustomerErrors {

    public static final Error NAME_REQUIRED =
        Error.validation("customer.name_required", "Customer name must not be blank");

    public static final Error EMAIL_INVALID =
        Error.validation("customer.email_invalid", "Customer email must be a valid address");

    public static final Error EMAIL_TAKEN =
        Error.conflict("customer.email_taken", "A customer with this email already exists");

    private CustomerErrors() {
    }

    public static Error notFound(UUID id) {
        return Error.notFound("customer.not_found", "Customer with id " + id + " was not found");
    }
}
