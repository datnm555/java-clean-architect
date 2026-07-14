package com.example.application.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.domain.products.Product;
import com.example.domain.products.ProductErrors;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Mock
    private ProductRepository products;

    @Mock
    private DateTimeProvider clock;

    @Test
    void validCommandSavesProductAndReturnsItsId() {
        when(clock.now()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
        CreateProductUseCase useCase = new CreateProductUseCase(products, clock);

        Result<UUID> result =
            useCase.handle(new CreateProductCommand("Keyboard", new BigDecimal("49.90")));

        assertThat(result.isSuccess()).isTrue();
        ArgumentCaptor<Product> saved = ArgumentCaptor.forClass(Product.class);
        verify(products).save(saved.capture());
        assertThat(saved.getValue().id()).isEqualTo(result.value());
        assertThat(saved.getValue().name()).isEqualTo("Keyboard");
    }

    @Test
    void invalidCommandFailsAndSavesNothing() {
        when(clock.now()).thenReturn(Instant.parse("2026-01-01T00:00:00Z"));
        CreateProductUseCase useCase = new CreateProductUseCase(products, clock);

        Result<UUID> result =
            useCase.handle(new CreateProductCommand("Keyboard", BigDecimal.ZERO));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.error()).isEqualTo(ProductErrors.PRICE_INVALID);
        verify(products, never()).save(any());
    }
}
