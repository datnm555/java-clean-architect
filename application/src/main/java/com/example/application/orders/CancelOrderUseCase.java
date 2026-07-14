package com.example.application.orders;

import com.example.domain.orders.Order;
import com.example.domain.orders.OrderErrors;
import com.example.sharedkernel.Result;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderUseCase {

    private final OrderRepository orders;

    CancelOrderUseCase(OrderRepository orders) {
        this.orders = orders;
    }

    @Transactional
    public Result<Void> handle(CancelOrderCommand command) {
        Optional<Order> found = orders.findById(command.orderId());
        if (found.isEmpty()) {
            return Result.failure(OrderErrors.notFound(command.orderId()));
        }
        Order order = found.get();
        Result<Void> cancelled = order.cancel();
        if (cancelled.isFailure()) {
            return cancelled;
        }
        orders.save(order);
        return Result.success();
    }
}
