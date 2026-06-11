package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
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

    private String description;

    @NotNull
    private UUID segmentId;

    private CampaignStatus status;

    private OffsetDateTime scheduledAt;

    @NotEmpty
    private String[] channels;

    private String[] tags;

    private Map<String, Object> utmParams;
}
