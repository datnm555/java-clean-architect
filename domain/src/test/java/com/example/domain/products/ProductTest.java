package com.example.domain.products;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sharedkernel.Result;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class ProductTest {

    private static final Instant NOW = Instant.parse("2026-01-01T00:00:00Z");

    @Test
    void createBuildsProductAndRaisesEvent() {
        Result<Product> result = Product.create("Keyboard", new BigDecimal("49.90"), NOW);

        assertThat(result.isSuccess()).isTrue();
        Product product = result.value();
        assertThat(product.id()).isNotNull();
        assertThat(product.name()).isEqualTo("Keyboard");
        assertThat(product.price()).isEqualByComparingTo("49.90");
        assertThat(product.createdAt()).isEqualTo(NOW);
        assertThat(product.pullDomainEvents())
            .containsExactly(new ProductCreatedDomainEvent(product.id()));
    }

    @Test
    void createTrimsTheName() {
        Product product = Product.create("  Mouse  ", new BigDecimal("10.00"), NOW).value();

        assertThat(product.name()).isEqualTo("Mouse");
    }

    @Test
    void createRejectsBlankName() {
        Result<Product> result = Product.create("   ", new BigDecimal("10.00"), NOW);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.error()).isEqualTo(ProductErrors.NAME_REQUIRED);
    }

    @Test
    void createRejectsNonPositivePrice() {
        assertThat(Product.create("Keyboard", BigDecimal.ZERO, NOW).error())
            .isEqualTo(ProductErrors.PRICE_INVALID);
        assertThat(Product.create("Keyboard", new BigDecimal("-1"), NOW).error())
            .isEqualTo(ProductErrors.PRICE_INVALID);
        assertThat(Product.create("Keyboard", null, NOW).error())
            .isEqualTo(ProductErrors.PRICE_INVALID);
    }
}
