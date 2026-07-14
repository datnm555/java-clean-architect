package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EntityScan("com.example.domain")
@EnableJpaRepositories("com.example.infrastructure")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
