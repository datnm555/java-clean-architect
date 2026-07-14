package com.example.application.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.application.products.ProductRepository;
import com.example.domain.orders.Order;
import com.example.domain.products.Product;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.ErrorType;
import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlaceOrderUseCaseTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Mock
    private OrderRepository orders;

    @Mock
    private ProductRepository products;

    @Mock
    private DateTimeProvider clock;

    private static Product product(String price) {
        return Product.create("Product", new BigDecimal(price), NOW).value();
    }

    @Test
    void pricesLinesFromTheCatalogAndSavesTheOrder() {
        Product keyboard = product("49.90");
        Product mouse = product("10.10");
        when(products.findById(keyboard.id())).thenReturn(Optional.of(keyboard));
        when(products.findById(mouse.id())).thenReturn(Optional.of(mouse));
        when(clock.now()).thenReturn(NOW);
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(orders, products, clock);

        Result<UUID> result = useCase.handle(new PlaceOrderCommand(UUID.randomUUID(), List.of(
            new PlaceOrderCommand.Line(keyboard.id(), 2),
            new PlaceOrderCommand.Line(mouse.id(), 1))));

        assertThat(result.isSuccess()).isTrue();
        ArgumentCaptor<Order> saved = ArgumentCaptor.forClass(Order.class);
        verify(orders).save(saved.capture());
        assertThat(saved.getValue().total()).isEqualByComparingTo("109.90");
    }

    @Test
    void failsWithNotFoundWhenAProductIsMissing() {
        UUID unknownProduct = UUID.randomUUID();
        when(products.findById(unknownProduct)).thenReturn(Optional.empty());
        PlaceOrderUseCase useCase = new PlaceOrderUseCase(orders, products, clock);

        Result<UUID> result = useCase.handle(new PlaceOrderCommand(UUID.randomUUID(),
            List.of(new PlaceOrderCommand.Line(unknownProduct, 1))));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.error().type()).isEqualTo(ErrorType.NOT_FOUND);
        verify(orders, never()).save(any());
    }
}
