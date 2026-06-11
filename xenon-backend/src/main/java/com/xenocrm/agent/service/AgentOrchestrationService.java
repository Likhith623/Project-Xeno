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

import java.time.OffsetDateTime;
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
                    .goal(requestDto.getPrompt())
                    .status(AgentSessionStatus.RUNNING)
                    .modelUsed("gemini-2.5-pro")
                    .conversationLog(new ArrayList<>())
                    .plan(new HashMap<>())
                    .startedAt(OffsetDateTime.now())
                    .build();
            session = sessionRepository.save(session);
        } else {
            session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new IllegalArgumentException("Session not found"));
        }

        log.info("Processing chat for session {}", session.getId());

        // Call Gemini
        String llmResponse = llmGatewayService.callGemini(requestDto.getPrompt());

        return AgentChatResponseDto.builder()
                .sessionId(session.getId())
                .textReply(llmResponse)
                .actionTaken(AgentDecisionType.SEGMENT_QUERY)
                .build();
    }
}
