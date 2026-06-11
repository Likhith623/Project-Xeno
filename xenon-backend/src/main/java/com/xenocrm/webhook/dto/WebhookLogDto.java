package com.xenocrm.webhook.dto;

import com.xenocrm.webhook.enums.WebhookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * WebhookLogDto — Standard DTO representing a webhook execution log.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookLogDto {
    private UUID id;
    private UUID webhookId;
    private Map<String, Object> payload;
    private WebhookStatus status;
    private Integer responseCode;
    private String responseBody;
    private OffsetDateTime createdAt;
}
