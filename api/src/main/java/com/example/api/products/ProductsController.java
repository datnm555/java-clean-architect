package com.example.api.products;

import com.example.api.error.ApiResponses;
import com.example.application.products.CreateProductCommand;
import com.example.application.products.CreateProductUseCase;
import com.example.application.products.GetProductQuery;
import com.example.application.products.GetProductUseCase;
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
@RequestMapping("/products")
class ProductsController {

    private final CreateProductUseCase createProduct;
    private final GetProductUseCase getProduct;

    ProductsController(CreateProductUseCase createProduct, GetProductUseCase getProduct) {
        this.createProduct = createProduct;
        this.getProduct = getProduct;
    }

    @PostMapping
    ResponseEntity<Object> create(@Valid @RequestBody CreateProductCommand command) {
        return ApiResponses.toResponse(createProduct.handle(command),
            id -> ResponseEntity.created(URI.create("/products/" + id)).body(new Created(id)));
    }

    @GetMapping("/{id}")
    ResponseEntity<Object> get(@PathVariable UUID id) {
        return ApiResponses.toResponse(getProduct.handle(new GetProductQuery(id)),
            response -> ResponseEntity.ok(response));
    }

    record Created(UUID id) {
    }
}
