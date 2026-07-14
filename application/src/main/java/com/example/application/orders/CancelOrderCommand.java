package com.example.application.orders;

import java.util.UUID;

public record CancelOrderCommand(UUID orderId) {
}
