package com.xenocrm.agent.dto;

import com.xenocrm.agent.enums.AgentDecisionType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AgentChatResponseDto {
    private UUID sessionId;
    private String textReply;
    private AgentDecisionType actionTaken;
}
