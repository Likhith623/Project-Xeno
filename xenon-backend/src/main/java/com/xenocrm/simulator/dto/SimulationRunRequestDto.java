package com.xenocrm.simulator.dto;

import lombok.Data;
import java.util.Map;
import java.util.UUID;

@Data
public class SimulationRunRequestDto {
    private UUID campaignId;
    private int syntheticAudienceSize;
    private Map<String, Object> personaDistribution;
}
