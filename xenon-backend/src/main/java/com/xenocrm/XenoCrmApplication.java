package com.xenocrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * XenoCrmApplication — Main entry point for the Xeno Mini CRM backend.
 * Layer: Boot
 * Purpose: Bootstraps the Spring Boot application and enables async processing and scheduling.
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class XenoCrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(XenoCrmApplication.class, args);
    }
}
