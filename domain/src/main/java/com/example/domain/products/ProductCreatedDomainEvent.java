package com.example.domain.products;

import com.example.sharedkernel.DomainEvent;
import java.util.UUID;

public record ProductCreatedDomainEvent(UUID productId) implements DomainEvent {
}
