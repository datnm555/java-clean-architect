package com.example.domain.customers;

import com.example.sharedkernel.DomainEvent;
import java.util.UUID;

public record CustomerRegisteredDomainEvent(UUID customerId) implements DomainEvent {
}
