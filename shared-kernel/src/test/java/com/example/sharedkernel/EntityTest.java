package com.example.sharedkernel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EntityTest {

    private record SomethingHappened(String what) implements DomainEvent {
    }

    private static final class TestAggregate extends AggregateRoot {
        void doSomething() {
            raise(new SomethingHappened("did it"));
        }
    }

    @Test
    void pullDomainEventsReturnsAndClears() {
        TestAggregate aggregate = new TestAggregate();
        aggregate.doSomething();

        assertThat(aggregate.pullDomainEvents()).containsExactly(new SomethingHappened("did it"));
        assertThat(aggregate.pullDomainEvents()).isEmpty();
    }
}
