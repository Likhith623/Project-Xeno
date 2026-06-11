package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CampaignResponseDto — Standard DTO representing a campaign.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResponseDto {
    private UUID id;
    private String name;
    private CampaignStatus status;
    private UUID segmentId;
    private BigDecimal budget;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String goal;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
