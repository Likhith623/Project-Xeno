package com.xenocrm.variant.repository;

import java.util.UUID;
import java.math.BigDecimal;

public interface MabStatsProjection {
    UUID getVariantId();
    UUID getCampaignId();
    String getVariantName();
    String getChannel();
    BigDecimal getMabAlpha();
    BigDecimal getMabBeta();
    Integer getMabImpressions();
    Integer getMabConversions();
    BigDecimal getExpectedConversionRate();
    BigDecimal getCiHalfWidth95();
    Boolean getMabIsActive();
    String getCampaignName();
}
