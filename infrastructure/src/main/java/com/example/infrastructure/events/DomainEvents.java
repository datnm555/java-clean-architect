package com.example.infrastructure.events;

import com.example.sharedkernel.Entity;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publishes an aggregate's pending domain events through Spring's event bus. Called by
 * repository adapters right after save, inside the transaction; listeners annotated
 * {@code @TransactionalEventListener(phase = AFTER_COMMIT)} receive them only once the
 * transaction has committed (at-most-once-after-commit — no Outbox until needed).
 */
@Component
public class DomainEvents {

    private final ApplicationEventPublisher publisher;

    DomainEvents(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishFrom(Entity entity) {
        entity.pullDomainEvents().forEach(publisher::publishEvent);
    }
}
