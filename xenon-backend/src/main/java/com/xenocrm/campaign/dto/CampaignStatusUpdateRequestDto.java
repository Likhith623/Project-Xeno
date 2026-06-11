package com.xenocrm.campaign.dto;

import com.xenocrm.campaign.enums.CampaignStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** CampaignStatusUpdateRequestDto -- DTO for changing campaign status. Layer: DTO */
@Data
public class CampaignStatusUpdateRequestDto {
    @NotNull private CampaignStatus status;
}
