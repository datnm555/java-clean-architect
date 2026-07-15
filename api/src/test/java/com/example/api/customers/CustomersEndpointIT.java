package com.example.api.customers;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.IntegrationTest;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

class CustomersEndpointIT extends IntegrationTest {

    @Test
    @SuppressWarnings("unchecked")
    void registerThenGetRoundTripAndDuplicateEmailConflicts() {
        ResponseEntity<Map> created = rest.postForEntity("/customers",
            Map.of("name", "Dat Nguyen", "email", "Dat@Vault22.com"), Map.class);

        assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        String id = (String) created.getBody().get("id");

        ResponseEntity<Map> fetched = rest.getForEntity("/customers/" + id, Map.class);
        assertThat(fetched.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetched.getBody())
            .containsEntry("name", "Dat Nguyen")
            .containsEntry("email", "dat@vault22.com"); // normalized by the aggregate

        ResponseEntity<ProblemDetail> duplicate = rest.postForEntity("/customers",
            Map.of("name", "Someone Else", "email", "dat@vault22.com"), ProblemDetail.class);
        assertThat(duplicate.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(duplicate.getBody().getProperties())
            .containsEntry("code", "customer.email_taken");
    }

    @Test
    void registeringWithABadEmailReturns400() {
        ResponseEntity<ProblemDetail> response = rest.postForEntity("/customers",
            Map.of("name", "Dat", "email", "not-an-email"), ProblemDetail.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
