package com.example.domain.orders;

import com.example.sharedkernel.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
public record OrderLine(
    @Column(nullable = false) UUID productId,
    @Column(nullable = false) int quantity,
    @Column(nullable = false, precision = 12, scale = 2) BigDecimal unitPrice)
    implements ValueObject {

    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
