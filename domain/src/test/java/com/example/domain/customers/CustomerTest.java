package com.example.domain.customers;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sharedkernel.Result;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CustomerTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void registerBuildsCustomerAndRaisesEvent() {
        Result<Customer> result = Customer.register("Dat Nguyen", "Dat@Example.com", NOW);

        assertThat(result.isSuccess()).isTrue();
        Customer customer = result.value();
        assertThat(customer.name()).isEqualTo("Dat Nguyen");
        assertThat(customer.email()).isEqualTo("dat@example.com"); // normalized
        assertThat(customer.pullDomainEvents())
            .containsExactly(new CustomerRegisteredDomainEvent(customer.id()));
    }

    @Test
    void registerRequiresName() {
        assertThat(Customer.register("  ", "dat@example.com", NOW).error())
            .isEqualTo(CustomerErrors.NAME_REQUIRED);
    }

    @Test
    void registerRejectsInvalidEmails() {
        assertThat(Customer.register("Dat", "not-an-email", NOW).error())
            .isEqualTo(CustomerErrors.EMAIL_INVALID);
        assertThat(Customer.register("Dat", "a@b", NOW).error())
            .isEqualTo(CustomerErrors.EMAIL_INVALID);
        assertThat(Customer.register("Dat", null, NOW).error())
            .isEqualTo(CustomerErrors.EMAIL_INVALID);
    }
}
