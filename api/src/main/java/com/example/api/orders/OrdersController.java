package com.example.api.orders;

import com.example.api.error.ApiResponses;
import com.example.application.orders.CancelOrderCommand;
import com.example.application.orders.CancelOrderUseCase;
import com.example.application.orders.GetOrderQuery;
import com.example.application.orders.GetOrderUseCase;
import com.example.application.orders.PlaceOrderCommand;
import com.example.application.orders.PlaceOrderUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
class OrdersController {

    private final PlaceOrderUseCase placeOrder;
    private final GetOrderUseCase getOrder;
    private final CancelOrderUseCase cancelOrder;

    OrdersController(PlaceOrderUseCase placeOrder, GetOrderUseCase getOrder,
        CancelOrderUseCase cancelOrder) {
        this.placeOrder = placeOrder;
        this.getOrder = getOrder;
        this.cancelOrder = cancelOrder;
    }

    @PostMapping
    ResponseEntity<Object> place(@Valid @RequestBody PlaceOrderCommand command) {
        return ApiResponses.toResponse(placeOrder.handle(command),
            id -> ResponseEntity.created(URI.create("/orders/" + id)).body(new Created(id)));
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> get(@PathVariable UUID id) {
        return ApiResponses.toResponse(getOrder.handle(new GetOrderQuery(id)),
            response -> ResponseEntity.ok(response));
    }

    @PostMapping("/{id}/cancel")
    ResponseEntity<Object> cancel(@PathVariable UUID id) {
        return ApiResponses.toResponse(cancelOrder.handle(new CancelOrderCommand(id)),
            ignored -> ResponseEntity.noContent().build());
    }

    record Created(UUID id) {
    }
}
