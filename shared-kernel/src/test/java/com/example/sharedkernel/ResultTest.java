package com.example.sharedkernel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import org.junit.jupiter.api.Test;

class ResultTest {

    private static final Error NOT_FOUND = Error.notFound("thing.not_found", "Thing was not found");

    @Test
    void successExposesValue() {
        Result<String> result = Result.success("hello");

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
        assertThat(result.value()).isEqualTo("hello");
    }

    @Test
    void failureExposesError() {
        Result<String> result = Result.failure(NOT_FOUND);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.error()).isEqualTo(NOT_FOUND);
    }

    @Test
    void readingValueOfFailureThrows() {
        Result<String> result = Result.failure(NOT_FOUND);

        assertThatIllegalStateException().isThrownBy(result::value);
    }

    @Test
    void readingErrorOfSuccessThrows() {
        Result<String> result = Result.success("hello");

        assertThatIllegalStateException().isThrownBy(result::error);
    }

    @Test
    void mapTransformsSuccessAndPropagatesFailure() {
        assertThat(Result.success(2).map(v -> v * 21).value()).isEqualTo(42);
        assertThat(Result.<Integer>failure(NOT_FOUND).map(v -> v * 21).error()).isEqualTo(NOT_FOUND);
    }

    @Test
    void foldSelectsTheMatchingBranch() {
        String onSuccess = Result.success("v").fold(v -> "ok:" + v, e -> "err");
        String onFailure = Result.failure(NOT_FOUND).fold(v -> "ok", Error::code);

        assertThat(onSuccess).isEqualTo("ok:v");
        assertThat(onFailure).isEqualTo("thing.not_found");
    }
}
