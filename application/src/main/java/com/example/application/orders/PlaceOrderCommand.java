package com.example.application.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.UUID;

public record PlaceOrderCommand(
    @NotNull UUID customerId,
    @NotEmpty List<@Valid Line> lines) {

    public record Line(@NotNull UUID productId, @Positive int quantity) {
    }
}
