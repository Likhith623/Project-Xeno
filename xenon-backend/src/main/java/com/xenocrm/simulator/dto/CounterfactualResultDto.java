package com.xenocrm.simulator.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CounterfactualResultDto {
    private String channel;
    private BigDecimal predictedOpenRate;
    private BigDecimal predictedCtr;
    private BigDecimal predictedConversionRate;
    private BigDecimal predictedRevenue;
    private String reasoning;
}
