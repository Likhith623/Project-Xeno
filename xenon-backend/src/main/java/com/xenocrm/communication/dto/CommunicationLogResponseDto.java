package com.xenocrm.communication.dto;

import com.xenocrm.communication.enums.CommunicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CommunicationLogResponseDto — Standard DTO representing a communication log.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationLogResponseDto {
    private UUID id;
    private UUID customerId;
    private UUID campaignId;
    private UUID variantId;
    private String channel;
    private CommunicationStatus status;
    private OffsetDateTime sentAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime openedAt;
    private OffsetDateTime clickedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
