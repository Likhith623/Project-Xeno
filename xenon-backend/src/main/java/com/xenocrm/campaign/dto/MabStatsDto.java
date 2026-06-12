package com.xenocrm.campaign.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class MabStatsDto {
    private UUID variantId;
    private UUID campaignId;
    private String variantName;
    private String channel;
    private BigDecimal mabAlpha;
    private BigDecimal mabBeta;
    private int mabImpressions;
    private int mabConversions;
    private BigDecimal expectedConversionRate;
    private BigDecimal ciHalfWidth95;
    private boolean mabIsActive;
    private String campaignName;
}
