package com.example.sharedkernel;

/**
 * Marker for domain events. Raised on an aggregate via {@link Entity#raise}; dispatched
 * after commit by infrastructure.
 */
public interface DomainEvent {
}
