package com.example.application.products;

import com.example.domain.products.Product;
import com.example.sharedkernel.DateTimeProvider;
import com.example.sharedkernel.Result;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateProductUseCase {

    private final ProductRepository products;
    private final DateTimeProvider clock;

    CreateProductUseCase(ProductRepository products, DateTimeProvider clock) {
        this.products = products;
        this.clock = clock;
    }

    @Transactional
    public Result<UUID> handle(CreateProductCommand command) {
        return Product.create(command.name(), command.price(), clock.now())
            .map(product -> {
                products.save(product);
                return product.id();
            });
    }
}
