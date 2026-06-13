package com.xenocrm.agent.controller;

import com.xenocrm.agent.entity.AgentDecisionEntity;
import com.xenocrm.agent.repository.AgentDecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class AgentDecisionController {

    private final AgentDecisionRepository decisionRepository;

    @GetMapping("/sessions/{sessionId}/decisions")
    public ResponseEntity<List<AgentDecisionEntity>> getDecisions(@PathVariable UUID sessionId) {
        List<AgentDecisionEntity> decisions = decisionRepository.findAllBySessionIdOrderByStepNumberAsc(sessionId);
        return ResponseEntity.ok(decisions);
    }
}
