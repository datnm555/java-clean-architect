package com.example.domain.orders;

import com.example.sharedkernel.AggregateRoot;
import com.example.sharedkernel.Result;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order extends AggregateRoot {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private OrderStatus status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_lines", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderLine> lines = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private Instant placedAt;

    protected Order() {
        // JPA only
    }

    private Order(UUID id, UUID customerId, List<OrderLine> lines, BigDecimal total,
        Instant placedAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = OrderStatus.PLACED;
        this.lines = new ArrayList<>(lines);
        this.total = total;
        this.placedAt = placedAt;
    }

    public static Result<Order> place(UUID customerId, List<OrderLine> lines, Instant placedAt) {
        if (customerId == null) {
            return Result.failure(OrderErrors.CUSTOMER_REQUIRED);
        }
        if (lines == null || lines.isEmpty()) {
            return Result.failure(OrderErrors.LINES_REQUIRED);
        }
        if (lines.stream().anyMatch(line -> line.quantity() <= 0)) {
            return Result.failure(OrderErrors.QUANTITY_INVALID);
        }
        BigDecimal total = lines.stream()
            .map(OrderLine::subtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = new Order(UUID.randomUUID(), customerId, lines, total, placedAt);
        order.raise(new OrderPlacedDomainEvent(order.id, customerId, total));
        return Result.success(order);
    }

    public Result<Void> cancel() {
        if (status != OrderStatus.PLACED) {
            return Result.failure(OrderErrors.CANNOT_CANCEL);
        }
        status = OrderStatus.CANCELLED;
        raise(new OrderCancelledDomainEvent(id));
        return Result.success();
    }

    public UUID id() {
        return id;
    }

    public UUID customerId() {
        return customerId;
    }

    public OrderStatus status() {
        return status;
    }

    public List<OrderLine> lines() {
        return List.copyOf(lines);
    }

    public BigDecimal total() {
        return total;
    }

    public Instant placedAt() {
        return placedAt;
    }
}
