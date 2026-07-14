package com.example.infrastructure.time;

import com.example.sharedkernel.DateTimeProvider;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
class SystemDateTimeProvider implements DateTimeProvider {

    @Override
    public Instant now() {
        return Instant.now();
    }
}
