package com.xenocrm.communication.dto;

import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.variant.enums.MessageChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationResponseDto {

    private UUID id;
    private UUID campaignId;
    private UUID variantId;
    private UUID customerId;
    private MessageChannel channel;
    private CommunicationStatus status;

    private String channelMessageId;
    private String recipientAddress;
    private String personalisedSubject;
    private String personalisedBody;

    private OffsetDateTime sentAt;
    private OffsetDateTime deliveredAt;
    private OffsetDateTime failedAt;
    private OffsetDateTime openedAt;
    private OffsetDateTime readAt;
    private OffsetDateTime clickedAt;
    private OffsetDateTime convertedAt;
    private OffsetDateTime unsubscribedAt;

    private String failureReason;
    private String failureCode;
    private int retryCount;
    private OffsetDateTime nextRetryAt;

    private UUID attributedOrderId;
    private int attributionWindowHours;
    private UUID spawnedFollowupId;
    private BigDecimal mabSampleValue;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
