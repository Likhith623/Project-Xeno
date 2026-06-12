package com.xenocrm.agent.controller;

import com.xenocrm.agent.dto.AgentChatRequestDto;
import com.xenocrm.agent.dto.AgentChatResponseDto;
import com.xenocrm.agent.dto.AgentDecisionResponseDto;
import com.xenocrm.agent.dto.AgentSessionResponseDto;
import com.xenocrm.agent.entity.AgentDecisionEntity;
import com.xenocrm.agent.entity.AgentSessionEntity;
import com.xenocrm.agent.repository.AgentDecisionRepository;
import com.xenocrm.agent.repository.AgentSessionRepository;
import com.xenocrm.agent.service.AgentOrchestrationService;
import com.xenocrm.common.ResponseWrapper;
import com.xenocrm.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AgentController — API endpoints for interacting with the Sovereign Agent.
 */
@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
@Tag(name = "Sovereign Agent", description = "Endpoints for AI conversational interface and orchestration")
public class AgentController {

    private final AgentOrchestrationService orchestrationService;
    private final AgentSessionRepository sessionRepository;
    private final AgentDecisionRepository decisionRepository;

    @PostMapping("/chat")
    @Operation(summary = "Send a prompt to the sovereign agent")
    public ResponseEntity<ResponseWrapper<AgentChatResponseDto>> processChat(@RequestBody AgentChatRequestDto requestDto) {
        return ResponseEntity.ok(ResponseWrapper.success(orchestrationService.processChat(requestDto), "Chat processed successfully"));
    }

    @GetMapping("/sessions/{id}")
    @Operation(summary = "Poll the status and plan of a sovereign agent session")
    public ResponseEntity<ResponseWrapper<AgentSessionResponseDto>> getSession(@PathVariable UUID id) {
        AgentSessionEntity session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AgentSession", "id", id));

        AgentSessionResponseDto dto = AgentSessionResponseDto.builder()
                .id(session.getId())
                .goal(session.getGoal())
                .status(session.getStatus())
                .modelUsed(session.getModelUsed())
                .plan(session.getPlan())
                .createdSegmentId(session.getCreatedSegmentId())
                .createdCampaignId(session.getCreatedCampaignId())
                .errorMessage(session.getErrorMessage())
                .startedAt(session.getStartedAt())
                .completedAt(session.getCompletedAt())
                .tokensUsedIn(session.getTokensUsedIn())
                .tokensUsedOut(session.getTokensUsedOut())
                .conversationLog(session.getConversationLog())
                .build();
        return ResponseEntity.ok(ResponseWrapper.success(dto));
    }

    @GetMapping("/sessions/{id}/decisions")
    @Operation(summary = "Get the full ReAct reasoning chain (decision audit trail) for a session")
    public ResponseEntity<ResponseWrapper<List<AgentDecisionResponseDto>>> getSessionDecisions(@PathVariable UUID id) {
        // Validate session exists
        if (!sessionRepository.existsById(id)) {
            throw new ResourceNotFoundException("AgentSession", "id", id);
        }

        List<AgentDecisionEntity> decisions = decisionRepository.findAllBySessionIdOrderByStepNumberAsc(id);

        List<AgentDecisionResponseDto> dtos = decisions.stream().map(d -> AgentDecisionResponseDto.builder()
                .id(d.getId())
                .sessionId(id)
                .stepNumber(d.getStepNumber())
                .decisionType(d.getDecisionType())
                .inputContext(d.getInputContext())
                .outputAction(d.getOutputAction())
                .reasoning(d.getReasoning())
                .createdAt(d.getCreatedAt())
                .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(ResponseWrapper.success(dtos));
    }
}
