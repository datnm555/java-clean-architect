package com.example.api.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API docs: Swagger UI at /swagger-ui.html, spec at /v3/api-docs.
 */
@Configuration
class OpenApiConfiguration {

    @Bean
    OpenAPI openApi() {
        return new OpenAPI().info(new Info()
            .title("java-clean-architect API")
            .description("Clean Architecture + DDD + Vertical Slice (CQRS-lite) template")
            .version("v1"));
    }
}
