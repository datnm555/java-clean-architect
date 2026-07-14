package com.example.domain.orders;

import com.example.sharedkernel.DomainEvent;
import java.util.UUID;

public record OrderCancelledDomainEvent(UUID orderId) implements DomainEvent {
}
