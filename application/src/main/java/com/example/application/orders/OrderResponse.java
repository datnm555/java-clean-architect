package com.example.application.orders;

import com.example.domain.orders.Order;
import com.example.domain.orders.OrderLine;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    UUID customerId,
    String status,
    BigDecimal total,
    Instant placedAt,
    List<Line> lines) {

    public record Line(UUID productId, int quantity, BigDecimal unitPrice) {

        static Line from(OrderLine line) {
            return new Line(line.productId(), line.quantity(), line.unitPrice());
        }
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
            order.id(),
            order.customerId(),
            order.status().name(),
            order.total(),
            order.placedAt(),
            order.lines().stream().map(Line::from).toList());
    }
}
