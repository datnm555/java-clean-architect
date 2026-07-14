package com.example.sharedkernel;

/**
 * Base for aggregate roots — the consistency boundary. Only aggregate roots are loaded
 * and saved through repositories, and only they raise domain events.
 */
public abstract class AggregateRoot extends Entity {
}
