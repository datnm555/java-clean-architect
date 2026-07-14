package com.example.sharedkernel;

import java.util.List;

/**
 * Bundles many field-level errors into a single {@link Error} of type VALIDATION.
 */
public final class ValidationError extends Error {

    private final List<Error> errors;

    private ValidationError(List<Error> errors) {
        super("validation.general", "One or more validation errors occurred", ErrorType.VALIDATION);
        this.errors = List.copyOf(errors);
    }

    public static ValidationError fromErrors(List<Error> errors) {
        return new ValidationError(errors);
    }

    public List<Error> errors() {
        return errors;
    }
}
