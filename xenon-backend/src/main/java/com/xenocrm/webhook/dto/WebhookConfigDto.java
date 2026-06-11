package com.xenocrm.webhook.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * WebhookConfigDto — DTO representing a webhook configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookConfigDto {
    private UUID id;
    
    @NotBlank
    private String url;
    
    private String secret;
    
    @NotBlank
    private String eventType;
    
    private Boolean isActive;
    
    private Integer retryCount;
    
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
