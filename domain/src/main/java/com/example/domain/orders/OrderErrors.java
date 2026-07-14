package com.example.domain.orders;

import com.example.sharedkernel.Error;
import java.util.UUID;

public final class OrderErrors {

    public static final Error CUSTOMER_REQUIRED =
        Error.validation("order.customer_required", "An order requires a customer");

    public static final Error LINES_REQUIRED =
        Error.validation("order.lines_required", "An order requires at least one line");

    public static final Error QUANTITY_INVALID =
        Error.validation("order.quantity_invalid", "Order line quantity must be greater than zero");

    public static final Error CANNOT_CANCEL =
        Error.conflict("order.cannot_cancel", "Only a placed order can be cancelled");

    private OrderErrors() {
    }

    public static Error notFound(UUID id) {
        return Error.notFound("order.not_found", "Order with id " + id + " was not found");
    }
}
