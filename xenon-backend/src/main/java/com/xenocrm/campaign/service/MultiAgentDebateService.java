package com.xenocrm.campaign.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Phase 7: Multi-Agent Debate (The War Room)
 * Spawns multiple AI personas to debate campaign strategies via prompt chaining
 * before presenting a finalized, heavily vetted compromise to the marketer.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MultiAgentDebateService {

    private final AgentLlmGatewayService llmGatewayService;

    /**
     * Executes the War Room debate for a given campaign goal.
     * @param campaignGoal A raw String describing the goal (e.g., "Win back churned winter buyers").
     * @return The final negotiated compromise strategy.
     */
    public String orchestrateDebate(String campaignGoal) {
        log.info("⚔️ THE WAR ROOM: Initiating Multi-Agent Debate for Goal: {}", campaignGoal);

        // Step 1: Agent A (Aggressive Marketer)
        log.info("Agent A (The Aggressive Marketer) is drafting initial strategy...");
        String promptA = "You are Agent A, a hyper-aggressive Chief Marketing Officer. Your goal is maximum immediate revenue and volume, " +
                "ignoring long-term brand damage or profit margin. Draft a 2-sentence campaign strategy for this goal: '" + campaignGoal + "'.";
        String strategyA = llmGatewayService.callGemini(promptA).trim();
        log.info("Agent A's Strategy:\n{}", strategyA);

        // Step 2: Agent B (Conservative CFO)
        log.info("Agent B (The Conservative CFO) is analyzing and critiquing Agent A...");
        String promptB = "You are Agent B, a deeply conservative Chief Financial Officer. You care entirely about profit margins, LTV, and brand exclusivity. " +
                "Agent A (the CMO) has just proposed the following strategy: '" + strategyA + "'. " +
                "Critique it harshly for destroying profit margins, and propose a conservative, low-discount 2-sentence alternative counter-strategy.";
        String strategyB = llmGatewayService.callGemini(promptB).trim();
        log.info("Agent B's Critique & Counter-Strategy:\n{}", strategyB);

        // Step 3: The Negotiator (Final Compromise)
        log.info("The Negotiator (CEO AI) is forging the final compromise...");
        String promptC = "You are the CEO AI. You must forge a compromise between two conflicting departments. " +
                "Agent A (Aggressive CMO) wants: '" + strategyA + "'. " +
                "Agent B (Conservative CFO) wants: '" + strategyB + "'. " +
                "Synthesize their ideas into a single, perfectly balanced 3-sentence campaign strategy that maximizes both immediate conversion AND protects profit margins. " +
                "Format your output as a final 'War Room Decision'.";
        String finalCompromise = llmGatewayService.callGemini(promptC).trim();
        log.info("FINAL WAR ROOM COMPROMISE:\n{}", finalCompromise);

        return "==== WAR ROOM DEBATE RECORD ====\n" +
                "Goal: " + campaignGoal + "\n\n" +
                "[Agent A - CMO]: " + strategyA + "\n\n" +
                "[Agent B - CFO]: " + strategyB + "\n\n" +
                "[FINAL DECISION - CEO]:\n" + finalCompromise + "\n" +
                "================================";
    }
}
