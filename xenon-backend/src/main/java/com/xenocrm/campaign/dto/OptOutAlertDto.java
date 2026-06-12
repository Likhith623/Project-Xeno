package com.xenocrm.campaign.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OptOutAlertDto {
    private UUID campaignId;
    private String campaignName;
    private BigDecimal optOutRateThreshold;
    private BigDecimal currentOptOutRatePct;
    private String alertLevel; // "OK", "WARNING", "EXCEEDED"
}
