package com.example.sharedkernel;

import java.time.Instant;

/**
 * Clock port so time is injectable and testable. Implemented in infrastructure.
 */
public interface DateTimeProvider {

    Instant now();
}
