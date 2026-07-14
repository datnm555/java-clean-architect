package com.example.domain.orders;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrderTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");
    private static final UUID CUSTOMER = UUID.randomUUID();

    private static OrderLine line(int quantity, String unitPrice) {
        return new OrderLine(UUID.randomUUID(), quantity, new BigDecimal(unitPrice));
    }

    @Test
    void placeComputesTotalAndRaisesEvent() {
        List<OrderLine> lines = List.of(line(2, "10.00"), line(1, "5.50"));

        Order order = Order.place(CUSTOMER, lines, NOW).value();

        assertThat(order.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(order.total()).isEqualByComparingTo("25.50");
        assertThat(order.lines()).hasSize(2);
        assertThat(order.pullDomainEvents()).containsExactly(
            new OrderPlacedDomainEvent(order.id(), CUSTOMER, new BigDecimal("25.50")));
    }

    @Test
    void placeRequiresCustomerAndLines() {
        assertThat(Order.place(null, List.of(line(1, "1.00")), NOW).error())
            .isEqualTo(OrderErrors.CUSTOMER_REQUIRED);
        assertThat(Order.place(CUSTOMER, List.of(), NOW).error())
            .isEqualTo(OrderErrors.LINES_REQUIRED);
        assertThat(Order.place(CUSTOMER, List.of(line(0, "1.00")), NOW).error())
            .isEqualTo(OrderErrors.QUANTITY_INVALID);
    }

    @Test
    void cancelTransitionsAndRaisesEvent() {
        Order order = Order.place(CUSTOMER, List.of(line(1, "1.00")), NOW).value();
        order.pullDomainEvents();

        Result<Void> result = order.cancel();

        assertThat(result.isSuccess()).isTrue();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.pullDomainEvents())
            .containsExactly(new OrderCancelledDomainEvent(order.id()));
    }

    @Test
    void cancellingTwiceConflicts() {
        Order order = Order.place(CUSTOMER, List.of(line(1, "1.00")), NOW).value();
        order.cancel();

        assertThat(order.cancel().error()).isEqualTo(OrderErrors.CANNOT_CANCEL);
    }
}
