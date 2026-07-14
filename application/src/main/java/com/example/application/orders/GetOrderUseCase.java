package com.example.application.orders;

import com.example.domain.orders.OrderErrors;
import com.example.sharedkernel.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetOrderUseCase {

    private final OrderRepository orders;

    GetOrderUseCase(OrderRepository orders) {
        this.orders = orders;
    }

    @Transactional(readOnly = true)
    public Result<OrderResponse> handle(GetOrderQuery query) {
        return orders.findById(query.id())
            .map(order -> Result.success(OrderResponse.from(order)))
            .orElseGet(() -> Result.failure(OrderErrors.notFound(query.id())));
    }
}
