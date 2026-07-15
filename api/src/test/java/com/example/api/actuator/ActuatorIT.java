package com.example.api.actuator;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.api.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ActuatorIT extends IntegrationTest {

    @Test
    void healthIsUpIncludingProbes() {
        ResponseEntity<String> health = rest.getForEntity("/actuator/health", String.class);
        assertThat(health.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(health.getBody()).contains("\"status\":\"UP\"");

        assertThat(rest.getForEntity("/actuator/health/liveness", String.class).getStatusCode())
            .isEqualTo(HttpStatus.OK);
        assertThat(rest.getForEntity("/actuator/health/readiness", String.class).getStatusCode())
            .isEqualTo(HttpStatus.OK);
    }
}
