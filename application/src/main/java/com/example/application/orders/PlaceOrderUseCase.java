package com.example.application.orders;

import com.example.application.products.ProductRepository;
import com.example.domain.orders.Order;
import com.example.domain.orders.OrderLine;
import com.example.domain.products.Product;
import com.example.domain.products.ProductErrors;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.Result;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlaceOrderUseCase {

    private final OrderRepository orders;
    private final ProductRepository products;
    private final DateTimeProvider clock;

    PlaceOrderUseCase(OrderRepository orders, ProductRepository products, DateTimeProvider clock) {
        this.orders = orders;
        this.products = products;
        this.clock = clock;
    }

    /**
     * Prices come from the product catalog, never from the client.
     */
    @Transactional
    public Result<UUID> handle(PlaceOrderCommand command) {
        List<OrderLine> lines = new ArrayList<>();
        for (PlaceOrderCommand.Line line : command.lines()) {
            Optional<Product> product = products.findById(line.productId());
            if (product.isEmpty()) {
                return Result.failure(ProductErrors.notFound(line.productId()));
            }
            lines.add(new OrderLine(line.productId(), line.quantity(), product.get().price()));
        }
        return Order.place(command.customerId(), lines, clock.now())
            .map(order -> {
                orders.save(order);
                return order.id();
            });
    }
}
