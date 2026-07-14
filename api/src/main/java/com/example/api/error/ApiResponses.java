package com.example.api.error;

import com.example.sharedkernel.Error;
import com.example.sharedkernel.Result;
import com.example.sharedkernel.ValidationError;
import java.util.List;
import java.util.function.Function;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

/**
 * Maps {@link Result} failures to RFC 7807 ProblemDetail responses. Controllers end with
 * {@code ApiResponses.toResponse(result, onSuccess)}.
 */
public final class ApiResponses {

    private ApiResponses() {
    }

    public static <T> ResponseEntity<Object> toResponse(
        Result<T> result, Function<T, ResponseEntity<Object>> onSuccess) {
        return result.fold(onSuccess, ApiResponses::toProblemResponse);
    }

    public static ResponseEntity<Object> toProblemResponse(Error error) {
        ProblemDetail problem = ProblemDetail.forStatus(statusOf(error));
        problem.setTitle(titleOf(error));
        problem.setDetail(error.description());
        problem.setProperty("code", error.code());
        if (error instanceof ValidationError validationError) {
            List<ProblemField> fields = validationError.errors().stream()
                .map(e -> new ProblemField(e.code(), e.description()))
                .toList();
            problem.setProperty("errors", fields);
        }
        return ResponseEntity.of(problem).build();
    }

    private static HttpStatus statusOf(Error error) {
        return switch (error.type()) {
            case VALIDATION, PROBLEM -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case FAILURE -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private static String titleOf(Error error) {
        return switch (error.type()) {
            case VALIDATION -> "Validation failed";
            case PROBLEM -> "Bad request";
            case NOT_FOUND -> "Not found";
            case CONFLICT -> "Conflict";
            case FAILURE -> "Server failure";
        };
    }

    record ProblemField(String code, String description) {
    }
}
