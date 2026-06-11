package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * CampaignResponseDto — Standard DTO representing a campaign and its metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDto {
    private UUID id;
    private String name;
    private String description;
    private UUID segmentId;
    private CampaignStatus status;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private String[] channels;
    private String[] tags;
    private Map<String, Object> utmParams;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
    // Metrics
    private Integer totalTargeted;
    private Integer totalSent;
    private Integer totalDelivered;
    private Integer totalFailed;
    private Integer totalOpened;
    private Integer totalClicked;
    private Integer totalOptOuts;
    private Integer totalConversions;
    private BigDecimal revenueGenerated;
    private OffsetDateTime lastMetricsUpdatedAt;
}
