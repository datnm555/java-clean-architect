package com.example.api.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OpenApiIT extends IntegrationTest {

    @Test
    void apiDocsDescribeTheSlices() {
        ResponseEntity<String> response = rest.getForEntity("/v3/api-docs", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .contains("\"/products\"")
            .contains("\"/orders\"")
            .contains("java-clean-architect API");
    }

    @Test
    void swaggerUiIsServed() {
        ResponseEntity<String> response = rest.getForEntity("/swagger-ui/index.html", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
