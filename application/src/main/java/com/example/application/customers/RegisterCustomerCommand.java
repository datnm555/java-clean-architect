package com.example.application.customers;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterCustomerCommand(
    @NotBlank String name,
    @NotBlank @Email String email) {
}
