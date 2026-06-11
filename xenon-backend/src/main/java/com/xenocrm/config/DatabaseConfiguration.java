package com.xenocrm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * DatabaseConfiguration — Configures database related beans.
 * Layer: Configuration
 * Purpose: Enables JPA auditing.
 */
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import java.time.OffsetDateTime;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class DatabaseConfiguration {

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }
}
