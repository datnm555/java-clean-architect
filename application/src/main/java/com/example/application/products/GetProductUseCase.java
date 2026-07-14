package com.example.application.products;

import com.example.domain.products.ProductErrors;
import com.example.sharedkernel.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetProductUseCase {

    private final ProductRepository products;

    GetProductUseCase(ProductRepository products) {
        this.products = products;
    }

    @Transactional(readOnly = true)
    public Result<ProductResponse> handle(GetProductQuery query) {
        return products.findById(query.id())
            .map(product -> Result.success(ProductResponse.from(product)))
            .orElseGet(() -> Result.failure(ProductErrors.notFound(query.id())));
    }
}
