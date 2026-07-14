package com.example.sharedkernel;

import java.util.ArrayList;
import java.util.List;

/**
 * Base for domain entities: carries the domain-event collection. Identity fields live in
 * the concrete entity (JPA requires @Id on a mapped class; this base class is
 * deliberately unmapped, so JPA ignores its fields).
 */
public abstract class Entity {

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();

    protected void raise(DomainEvent event) {
        domainEvents.add(event);
    }

    /** Returns the accumulated events and clears them — called once by infrastructure on save. */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
