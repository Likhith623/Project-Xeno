package com.xenocrm.agent.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AgentChatRequestDto {
    private String prompt;
    private UUID sessionId; // nullable, starts new session if null
}
