package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/** CampaignCreateRequestDto -- DTO for creating a new campaign. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CampaignCreateRequestDto {
    @NotBlank private String name;
    private String description;
    private UUID segmentId;
    private CampaignStatus status;
    private String goal;
    private OffsetDateTime scheduledAt;
    private String timezone;
    private Integer maxSendCount;
    private BigDecimal optOutRateThreshold;
}
