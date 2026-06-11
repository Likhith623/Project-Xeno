package com.xenocrm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApiConfiguration — Configures Swagger / OpenAPI documentation.
 * Layer: Configuration
 * Purpose: Customizes the OpenAPI generated documentation.
 */
@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Xeno Mini CRM API")
                        .version("1.0")
                        .description("API documentation for the Xeno AI-Native CRM backend"));
    }
}
