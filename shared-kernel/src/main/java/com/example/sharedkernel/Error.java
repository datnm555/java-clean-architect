package com.example.sharedkernel;

import java.util.Objects;

/**
 * A business failure: a stable machine-readable code, a human description and a type.
 * Class (not record) so {@link ValidationError} can extend it.
 */
public class Error {

    public static final Error NONE = new Error("", "", ErrorType.FAILURE);

    private final String code;
    private final String description;
    private final ErrorType type;

    protected Error(String code, String description, ErrorType type) {
        this.code = Objects.requireNonNull(code);
        this.description = Objects.requireNonNull(description);
        this.type = Objects.requireNonNull(type);
    }

    public static Error failure(String code, String description) {
        return new Error(code, description, ErrorType.FAILURE);
    }

    public static Error validation(String code, String description) {
        return new Error(code, description, ErrorType.VALIDATION);
    }

    public static Error problem(String code, String description) {
        return new Error(code, description, ErrorType.PROBLEM);
    }

    public static Error notFound(String code, String description) {
        return new Error(code, description, ErrorType.NOT_FOUND);
    }

    public static Error conflict(String code, String description) {
        return new Error(code, description, ErrorType.CONFLICT);
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public ErrorType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        return this == o
            || (o instanceof Error other
                && code.equals(other.code)
                && description.equals(other.description)
                && type == other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, description, type);
    }

    @Override
    public String toString() {
        return "Error[" + type + " " + code + ": " + description + "]";
    }
}
