package com.xenocrm.agent.dto;

import com.xenocrm.agent.enums.AgentSessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AgentSessionResponseDto {
    private UUID id;
    private String goal;
    private AgentSessionStatus status;
    private String modelUsed;
    private Map<String, Object> plan;
    private UUID createdSegmentId;
    private UUID createdCampaignId;
    private String errorMessage;
    private OffsetDateTime startedAt;
    private OffsetDateTime completedAt;
    private int tokensUsedIn;
    private int tokensUsedOut;
    private List<Map<String, Object>> conversationLog;
}
