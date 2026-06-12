package com.xenocrm.campaign.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class CampaignPerformanceDto {
    private UUID id;
    private String name;
    private String status;
    private String goal;
    private OffsetDateTime scheduledAt;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private boolean createdByAgent;
    // Raw counts
    private int totalSent;
    private int totalDelivered;
    private int totalFailed;
    private int totalOpened;
    private int totalRead;
    private int totalClicked;
    private int totalConverted;
    private BigDecimal revenueAttributed;
    // Computed rates (%)
    private BigDecimal deliveryRatePct;
    private BigDecimal failureRatePct;
    private BigDecimal openRatePct;
    private BigDecimal ctrPct;
    private BigDecimal conversionRatePct;
    private BigDecimal optOutRatePct;
    // Segment info
    private String segmentName;
    private Integer segmentSize;
}
