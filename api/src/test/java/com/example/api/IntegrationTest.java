package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base for full-stack integration tests: boots the real app against a shared
 * Testcontainers PostgreSQL — requires Docker.
 *
 * Deliberately NOT annotated with @Testcontainers/@Container: the singleton container is
 * started once here and shared by every IT class (the JUnit extension would stop it after
 * the first class while the cached Spring context still points at it). Ryuk reaps the
 * container when the JVM exits.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    @ServiceConnection
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine");

    static {
        POSTGRES.start();
    }

    @Autowired
    protected TestRestTemplate rest;
}
