package com.xenocrm.agent.dto;

import com.xenocrm.agent.enums.AgentDecisionType;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AgentDecisionResponseDto {
    private UUID id;
    private UUID sessionId;
    private int stepNumber;
    private AgentDecisionType decisionType;
    private Map<String, Object> inputContext;
    private Map<String, Object> outputAction;
    private String reasoning;
    private OffsetDateTime createdAt;
}
