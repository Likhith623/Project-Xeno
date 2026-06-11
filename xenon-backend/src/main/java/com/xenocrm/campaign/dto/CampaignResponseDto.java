package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** CampaignResponseDto -- Standard API response for a campaign. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CampaignResponseDto {
    private UUID id;
    private String name;
    private String description;
    private CampaignStatus status;
    private String goal;
    private UUID segmentId;
    private String segmentName;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private String timezone;
    private Integer maxSendCount;
    private BigDecimal optOutRateThreshold;
    private boolean createdByAgent;
    private String agentSessionId;
    private UUID parentCampaignId;
    private int totalSent;
    private int totalDelivered;
    private int totalFailed;
    private int totalOpened;
    private int totalRead;
    private int totalClicked;
    private int totalConverted;
    private BigDecimal revenueAttributed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
