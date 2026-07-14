package com.example.api.products;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.IntegrationTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

class ProductsEndpointIT extends IntegrationTest {

    @Test
    @SuppressWarnings("unchecked")
    void createThenGetRoundTrip() {
        ResponseEntity<Map> created = rest.postForEntity(
            "/products", Map.of("name", "Keyboard", "price", 49.90), Map.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String id = (String) created.getBody().get("id");
        assertThat(created.getHeaders().getLocation()).hasPath("/products/" + id);

        ResponseEntity<Map> fetched = rest.getForEntity("/products/" + id, Map.class);

        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody())
            .containsEntry("id", id)
            .containsEntry("name", "Keyboard");
    }

    @Test
    void gettingAnUnknownProductReturns404Problem() {
        ResponseEntity<ProblemDetail> response = rest.getForEntity(
            "/products/00000000-0000-0000-0000-000000000000", ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getProperties()).containsEntry("code", "product.not_found");
    }

    @Test
    void creatingWithInvalidBodyReturns400Problem() {
        ResponseEntity<ProblemDetail> response = rest.postForEntity(
            "/products", Map.of("name", "", "price", -1), ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getTitle()).isEqualTo("Validation failed");
    }
}
