package com.xenocrm.campaign.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/** OptOutAlertDto -- Maps v_opt_out_alerts view results. Layer: DTO */
@Data
public class OptOutAlertDto {
    private UUID campaignId;
    private String campaignName;
    private BigDecimal optOutRateThreshold;
    private BigDecimal currentOptOutRatePct;
    private String alertLevel;  // EXCEEDED, WARNING, OK
}
