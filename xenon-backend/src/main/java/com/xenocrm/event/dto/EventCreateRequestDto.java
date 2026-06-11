package com.xenocrm.event.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * EventCreateRequestDto — DTO for ingesting a new event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequestDto {

    private UUID customerId;

    @NotBlank
    private String eventType;

    @NotBlank
    private String source;

    private Map<String, Object> payload;

    private String sessionId;

    private String ipAddress;

    private String userAgent;
}
