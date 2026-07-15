package com.example.application.customers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.domain.customers.Customer;
import com.example.domain.customers.CustomerErrors;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.Result;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerUseCaseTest {

    @Mock
    private CustomerRepository customers;

    @Mock
    private DateTimeProvider clock;

    @Test
    void registersANewCustomer() {
        when(customers.existsByEmail("dat@example.com")).thenReturn(false);
        when(clock.now()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
        RegisterCustomerUseCase useCase = new RegisterCustomerUseCase(customers, clock);

        Result<UUID> result =
            useCase.handle(new RegisterCustomerCommand("Dat", "Dat@Example.com"));

        assertThat(result.isSuccess()).isTrue();
        ArgumentCaptor<Customer> saved = ArgumentCaptor.forClass(Customer.class);
        verify(customers).save(saved.capture());
        assertThat(saved.getValue().email()).isEqualTo("dat@example.com");
    }

    @Test
    void conflictsWhenTheEmailIsTaken() {
        when(customers.existsByEmail("dat@example.com")).thenReturn(true);
        RegisterCustomerUseCase useCase = new RegisterCustomerUseCase(customers, clock);

        Result<UUID> result =
            useCase.handle(new RegisterCustomerCommand("Dat", "dat@example.com"));

        assertThat(result.error()).isEqualTo(CustomerErrors.EMAIL_TAKEN);
        verify(customers, never()).save(any());
    }
}
