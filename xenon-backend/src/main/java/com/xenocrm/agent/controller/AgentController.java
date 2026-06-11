package com.xenocrm.agent.controller;

import com.xenocrm.agent.dto.AgentChatRequestDto;
import com.xenocrm.agent.dto.AgentChatResponseDto;
import com.xenocrm.agent.service.AgentOrchestrationService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AgentController — API endpoints for interacting with the Sovereign Agent.
 */
@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
@Tag(name = "Sovereign Agent", description = "Endpoints for AI conversational interface and orchestration")
public class AgentController {

    private final AgentOrchestrationService orchestrationService;

    @PostMapping("/chat")
    @Operation(summary = "Send a prompt to the sovereign agent")
    public ResponseEntity<ResponseWrapper<AgentChatResponseDto>> processChat(@RequestBody AgentChatRequestDto requestDto) {
        return ResponseEntity.ok(ResponseWrapper.success(orchestrationService.processChat(requestDto), "Chat processed successfully"));
    }
}
