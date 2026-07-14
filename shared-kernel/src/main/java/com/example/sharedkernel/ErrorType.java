package com.example.sharedkernel;

/**
 * Classifies an {@link Error}; the api module maps each type to an HTTP status.
 */
public enum ErrorType {
    FAILURE,
    VALIDATION,
    PROBLEM,
    NOT_FOUND,
    CONFLICT
}
