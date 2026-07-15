package com.example.api.customers;

import com.example.api.error.ApiResponses;
import com.example.application.customers.GetCustomerQuery;
import com.example.application.customers.GetCustomerUseCase;
import com.example.application.customers.RegisterCustomerCommand;
import com.example.application.customers.RegisterCustomerUseCase;
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
@RequestMapping("/customers")
class CustomersController {

    private final RegisterCustomerUseCase registerCustomer;
    private final GetCustomerUseCase getCustomer;

    CustomersController(RegisterCustomerUseCase registerCustomer, GetCustomerUseCase getCustomer) {
        this.registerCustomer = registerCustomer;
        this.getCustomer = getCustomer;
    }

    @PostMapping
    ResponseEntity<Object> register(@Valid @RequestBody RegisterCustomerCommand command) {
        return ApiResponses.toResponse(registerCustomer.handle(command),
            id -> ResponseEntity.created(URI.create("/customers/" + id)).body(new Created(id)));
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> get(@PathVariable UUID id) {
        return ApiResponses.toResponse(getCustomer.handle(new GetCustomerQuery(id)),
            response -> ResponseEntity.ok(response));
    }

    record Created(UUID id) {
    }
}
