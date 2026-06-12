package com.xenocrm.campaign.repository;

import java.util.UUID;
import java.time.OffsetDateTime;
import java.math.BigDecimal;

public interface CampaignPerformanceProjection {
    UUID getId();
    String getName();
    String getStatus();
    String getGoal();
    java.time.Instant getScheduledAt();
    java.time.Instant getStartedAt();
    java.time.Instant getCompletedAt();
    Boolean getCreatedByAgent();
    Integer getTotalSent();
    Integer getTotalDelivered();
    Integer getTotalFailed();
    Integer getTotalOpened();
    Integer getTotalRead();
    Integer getTotalClicked();
    Integer getTotalConverted();
    BigDecimal getRevenueAttributed();
    BigDecimal getDeliveryRatePct();
    BigDecimal getFailureRatePct();
    BigDecimal getOpenRatePct();
    BigDecimal getCtrPct();
    BigDecimal getConversionRatePct();
    BigDecimal getOptOutRatePct();
    String getSegmentName();
    Integer getSegmentSize();
}
