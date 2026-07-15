package com.example.application.customers;

import com.example.domain.customers.CustomerErrors;
import com.example.sharedkernel.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetCustomerUseCase {

    private final CustomerRepository customers;

    GetCustomerUseCase(CustomerRepository customers) {
        this.customers = customers;
    }

    @Transactional(readOnly = true)
    public Result<CustomerResponse> handle(GetCustomerQuery query) {
        return customers.findById(query.id())
            .map(customer -> Result.success(CustomerResponse.from(customer)))
            .orElseGet(() -> Result.failure(CustomerErrors.notFound(query.id())));
    }
}
