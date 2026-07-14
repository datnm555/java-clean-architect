package com.example.api.orders;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.IntegrationTest;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

class OrdersEndpointIT extends IntegrationTest {

    private String createProduct(String name, double price) {
        ResponseEntity<Map> response = rest.postForEntity(
            "/products", Map.of("name", name, "price", price), Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return (String) response.getBody().get("id");
    }

    @Test
    @SuppressWarnings("unchecked")
    void placeGetCancelLifecycle() {
        String keyboard = createProduct("Keyboard", 49.90);
        String mouse = createProduct("Mouse", 10.10);
        UUID customer = UUID.randomUUID();

        ResponseEntity<Map> placed = rest.postForEntity("/orders", Map.of(
            "customerId", customer.toString(),
            "lines", List.of(
                Map.of("productId", keyboard, "quantity", 2),
                Map.of("productId", mouse, "quantity", 1))), Map.class);
        assertThat(placed.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String orderId = (String) placed.getBody().get("id");

        ResponseEntity<Map> fetched = rest.getForEntity("/orders/" + orderId, Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody())
            .containsEntry("status", "PLACED")
            .containsEntry("total", 109.90);
        assertThat((List<Map<String, Object>>) fetched.getBody().get("lines")).hasSize(2);

        ResponseEntity<Void> cancelled =
            rest.postForEntity("/orders/" + orderId + "/cancel", null, Void.class);
        assertThat(cancelled.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<ProblemDetail> cancelledAgain =
            rest.postForEntity("/orders/" + orderId + "/cancel", null, ProblemDetail.class);
        assertThat(cancelledAgain.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(cancelledAgain.getBody().getProperties())
            .containsEntry("code", "order.cannot_cancel");
    }

    @Test
    void placingWithAnUnknownProductReturns404Problem() {
        ResponseEntity<ProblemDetail> response = rest.postForEntity("/orders", Map.of(
            "customerId", UUID.randomUUID().toString(),
            "lines", List.of(Map.of(
                "productId", "00000000-0000-0000-0000-000000000000", "quantity", 1))),
            ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getProperties()).containsEntry("code", "product.not_found");
    }

    @Test
    void placingWithoutLinesReturns400Problem() {
        ResponseEntity<ProblemDetail> response = rest.postForEntity("/orders", Map.of(
            "customerId", UUID.randomUUID().toString(), "lines", List.of()),
            ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
