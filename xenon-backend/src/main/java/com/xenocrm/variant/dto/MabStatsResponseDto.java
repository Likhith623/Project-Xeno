package com.xenocrm.variant.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/** MabStatsResponseDto -- Thompson Sampling stats from v_variant_mab_stats. Layer: DTO */
@Data
public class MabStatsResponseDto {
    private UUID id;
    private UUID campaignId;
    private String name;
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
