package com.example.api.error;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Catches what the Result path does not: bean-validation failures on request bodies and
 * unexpected exceptions (programmer errors). Business failures never travel as
 * exceptions — they are Results mapped by {@link ApiResponses}.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more request fields are invalid");
        List<ProblemFieldError> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(ProblemFieldError::of)
            .toList();
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception exception) {
        log.error("Unhandled exception", exception);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Server failure");
        problem.setDetail("An unexpected error occurred");
        return problem;
    }

    private record ProblemFieldError(String field, String message) {
        static ProblemFieldError of(FieldError error) {
            return new ProblemFieldError(error.getField(), error.getDefaultMessage());
        }
    }
}
