package com.example.domain.orders;

import com.example.sharedkernel.DomainEvent;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderPlacedDomainEvent(UUID orderId, UUID customerId, BigDecimal total)
    implements DomainEvent {
}
