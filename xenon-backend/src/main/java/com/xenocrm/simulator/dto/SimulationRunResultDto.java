package com.xenocrm.simulator.dto;

import com.xenocrm.simulator.enums.SimulationRunStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class SimulationRunResultDto {
    private UUID id;
    private UUID campaignId;
    private SimulationRunStatus status;
    private int syntheticAudienceSize;
    private BigDecimal predictedOpenRate;
    private BigDecimal predictedCtr;
    private BigDecimal predictedConversionRate;
    private BigDecimal predictedRevenue;
    private BigDecimal confidenceIntervalLow;
    private BigDecimal confidenceIntervalHigh;
    private UUID winningVariantId;
    private Map<String, Object> personaDistribution;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
}
