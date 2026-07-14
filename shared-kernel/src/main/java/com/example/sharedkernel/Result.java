package com.example.sharedkernel;

import java.util.Objects;
import java.util.function.Function;

/**
 * Success-or-failure outcome of a use case or domain operation. Business failures travel
 * as {@link Error} values, never as exceptions — exceptions are for programmer errors.
 */
public final class Result<T> {

    private final T value;
    private final Error error;

    private Result(T value, Error error) {
        this.value = value;
        this.error = error;
    }

    public static Result<Void> success() {
        return new Result<>(null, null);
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(Objects.requireNonNull(value), null);
    }

    public static <T> Result<T> failure(Error error) {
        return new Result<>(null, Objects.requireNonNull(error));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public boolean isFailure() {
        return error != null;
    }

    /** The success value; throws if this result is a failure — check first or use fold/map. */
    public T value() {
        if (isFailure()) {
            throw new IllegalStateException("Cannot read the value of a failure result: " + error);
        }
        return value;
    }

    /** The error; throws if this result is a success. */
    public Error error() {
        if (isSuccess()) {
            throw new IllegalStateException("Cannot read the error of a success result");
        }
        return error;
    }

    public <U> Result<U> map(Function<T, U> mapper) {
        return isSuccess() ? Result.success(mapper.apply(value)) : Result.failure(error);
    }

    public <U> Result<U> flatMap(Function<T, Result<U>> mapper) {
        return isSuccess() ? mapper.apply(value) : Result.failure(error);
    }

    public <U> U fold(Function<T, U> onSuccess, Function<Error, U> onFailure) {
        return isSuccess() ? onSuccess.apply(value) : onFailure.apply(error);
    }
}
