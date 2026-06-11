package com.xenocrm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * GeminiClientConfiguration — Creates the Spring RestClient bean for Gemini API calls.
 * Layer: Configuration
 * This is the ONLY place where the Gemini base URL and API key are configured.
 * All LLM calls go through AgentLlmGatewayService which injects this bean.
 */
@Configuration
public class GeminiClientConfiguration {

    @Value("${gemini.endpoint}")
    private String geminiBaseEndpoint;

    /**
     * Creates a pre-configured RestClient pointed at the Gemini REST API base URL.
     * The API key is added as a query parameter per request in AgentLlmGatewayService,
     * not here, so this client can be reused for different model endpoints.
     */
    @Bean(name = "geminiRestClient")
    public RestClient createGeminiRestClient() {
        return RestClient.builder()
            .baseUrl(geminiBaseEndpoint)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
