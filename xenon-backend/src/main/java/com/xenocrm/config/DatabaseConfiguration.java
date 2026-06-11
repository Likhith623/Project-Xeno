package com.xenocrm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * DatabaseConfiguration — Configures database related beans.
 * Layer: Configuration
 * Purpose: Enables JPA auditing.
 */
@Configuration
@EnableJpaAuditing
public class DatabaseConfiguration {
}
