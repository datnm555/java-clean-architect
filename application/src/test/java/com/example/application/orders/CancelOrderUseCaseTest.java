package com.example.application.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.domain.orders.Order;
import com.example.domain.orders.OrderErrors;
import com.example.domain.orders.OrderLine;
import com.example.domain.orders.OrderStatus;
import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelOrderUseCaseTest {

    @Mock
    private OrderRepository orders;

    private static Order placedOrder() {
        return Order.place(UUID.randomUUID(),
            List.of(new OrderLine(UUID.randomUUID(), 1, new BigDecimal("10.00"))),
            Instant.parse("2026-01-01T00:00:00Z")).value();
    }

    @Test
    void cancelsAPlacedOrderAndSavesIt() {
        Order order = placedOrder();
        when(orders.findById(order.id())).thenReturn(Optional.of(order));
        CancelOrderUseCase useCase = new CancelOrderUseCase(orders);

        Result<Void> result = useCase.handle(new CancelOrderCommand(order.id()));

        assertThat(result.isSuccess()).isTrue();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
        verify(orders).save(order);
    }

    @Test
    void conflictsWhenTheOrderIsAlreadyCancelled() {
        Order order = placedOrder();
        order.cancel();
        when(orders.findById(order.id())).thenReturn(Optional.of(order));
        CancelOrderUseCase useCase = new CancelOrderUseCase(orders);

        Result<Void> result = useCase.handle(new CancelOrderCommand(order.id()));

        assertThat(result.error()).isEqualTo(OrderErrors.CANNOT_CANCEL);
        verify(orders, never()).save(any());
    }

    @Test
    void failsWithNotFoundForAnUnknownOrder() {
        UUID id = UUID.randomUUID();
        when(orders.findById(id)).thenReturn(Optional.empty());
        CancelOrderUseCase useCase = new CancelOrderUseCase(orders);

        assertThat(useCase.handle(new CancelOrderCommand(id)).error())
            .isEqualTo(OrderErrors.notFound(id));
    }
}
