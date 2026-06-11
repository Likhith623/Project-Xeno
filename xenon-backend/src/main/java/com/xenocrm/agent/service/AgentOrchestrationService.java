package com.xenocrm.agent.service;

import com.xenocrm.agent.dto.AgentChatRequestDto;
import com.xenocrm.agent.dto.AgentChatResponseDto;
import com.xenocrm.agent.entity.AgentSessionEntity;
import com.xenocrm.agent.enums.AgentDecisionType;
import com.xenocrm.agent.enums.AgentSessionStatus;
import com.xenocrm.agent.repository.AgentSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * AgentOrchestrationService — Manages the conversational flow and coordinates tools.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgentOrchestrationService {

    private final AgentSessionRepository sessionRepository;
    private final AgentLlmGatewayService llmGatewayService;

    public AgentChatResponseDto processChat(AgentChatRequestDto requestDto) {
        UUID sessionId = requestDto.getSessionId();
        AgentSessionEntity session;

        if (sessionId == null) {
            session = AgentSessionEntity.builder()
                    .status(AgentSessionStatus.ACTIVE)
                    .messageHistory(new ArrayList<>())
                    .contextSnapshot(new HashMap<>())
                    .build();
            session = sessionRepository.save(session);
        } else {
            session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        }

        log.info("Processing chat for session {}", session.getId());

        // Call Gemini
        llmGatewayService.callGemini(requestDto.getPrompt(), String.class);

        return AgentChatResponseDto.builder()
                .sessionId(session.getId())
                .textReply("This is a mock reply from the Sovereign Agent.")
                .actionTaken(AgentDecisionType.GENERAL_QUERY)
                .build();
    }
}
