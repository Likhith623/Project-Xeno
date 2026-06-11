package com.xenocrm.campaign.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/** CampaignPerformanceSummaryDto -- Maps v_campaign_performance view results. Layer: DTO */
@Data
public class CampaignPerformanceSummaryDto {
    private UUID id;
    private String name;
    private String status;
    private Integer totalSent;
    private Integer totalDelivered;
    private Integer totalFailed;
    private Integer totalOpened;
    private Integer totalClicked;
    private Integer totalConverted;
    private BigDecimal revenueAttributed;
    private BigDecimal deliveryRatePct;
    private BigDecimal failureRatePct;
    private BigDecimal openRatePct;
    private BigDecimal ctrPct;
    private BigDecimal conversionRatePct;
    private BigDecimal optOutRatePct;
    private String segmentName;
    private Integer segmentSize;
}
