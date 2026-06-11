package com.xenocrm.channelstub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

/**
 * ChannelStubApplication — Main entry point for the Channel Stub.
 * Layer: Boot
 * Purpose: Bootstraps the Channel Stub on port 8081 without loading CRM beans.
 */
@SpringBootApplication(scanBasePackages = "com.xenocrm.channelstub")
@Profile("stub")
public class ChannelStubApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChannelStubApplication.class, args);
    }
}
