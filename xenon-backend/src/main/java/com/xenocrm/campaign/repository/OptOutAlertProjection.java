package com.xenocrm.campaign.repository;

import java.util.UUID;
import java.math.BigDecimal;

public interface OptOutAlertProjection {
    UUID getCampaignId();
    String getCampaignName();
    BigDecimal getOptOutRateThreshold();
    BigDecimal getCurrentOptOutRatePct();
    String getAlertLevel();
}
