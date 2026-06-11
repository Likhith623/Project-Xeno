package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CampaignCreateRequestDto — DTO for creating a new campaign.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CampaignCreateRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private UUID segmentId;

    private CampaignStatus status;

    private BigDecimal budget;

    private OffsetDateTime startDate;

    private OffsetDateTime endDate;

    private String goal;
}
