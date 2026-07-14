package com.example.application.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.domain.products.Product;
import com.example.sharedkernel.ErrorType;
import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProductUseCaseTest {

    @Mock
    private ProductRepository products;

    @Test
    void returnsResponseWhenProductExists() {
        Product product = Product
            .create("Keyboard", new BigDecimal("49.90"), Instant.parse("2026-01-01T00:00:00Z"))
            .value();
        when(products.findById(product.id())).thenReturn(Optional.of(product));
        GetProductUseCase useCase = new GetProductUseCase(products);

        Result<ProductResponse> result = useCase.handle(new GetProductQuery(product.id()));

        assertThat(result.value()).isEqualTo(ProductResponse.from(product));
    }

    @Test
    void failsWithNotFoundWhenProductIsMissing() {
        UUID id = UUID.randomUUID();
        when(products.findById(id)).thenReturn(Optional.empty());
        GetProductUseCase useCase = new GetProductUseCase(products);

        Result<ProductResponse> result = useCase.handle(new GetProductQuery(id));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.error().type()).isEqualTo(ErrorType.NOT_FOUND);
    }
}
