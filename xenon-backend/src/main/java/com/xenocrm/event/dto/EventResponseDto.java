package com.xenocrm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * EventResponseDto — Standard DTO representing an event log.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDto {
    private UUID id;
    private UUID customerId;
    private String eventType;
    private String source;
    private Map<String, Object> payload;
    private String sessionId;
    private String ipAddress;
    private String userAgent;
    private OffsetDateTime createdAt;
}
