package com.example.domain.products;

import com.example.sharedkernel.Error;
import java.util.UUID;

public final class ProductErrors {

    public static final Error NAME_REQUIRED =
        Error.validation("product.name_required", "Product name must not be blank");

    public static final Error PRICE_INVALID =
        Error.validation("product.price_invalid", "Product price must be greater than zero");

    private ProductErrors() {
    }

    public static Error notFound(UUID id) {
        return Error.notFound("product.not_found", "Product with id " + id + " was not found");
    }
}
