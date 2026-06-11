package com.xenocrm.correction.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.correction.dto.CorrectionDecisionDto;
import com.xenocrm.correction.enums.CorrectionActionType;
import com.xenocrm.correction.enums.CorrectionTriggerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * AiCorrectionDecisionService — Uses Gemini to decide the best corrective action.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiCorrectionDecisionService {

    private final AgentLlmGatewayService agentLlmGatewayService;
    private final CorrectionExecutionService correctionExecutionService;

    public void makeCorrectionDecision(CampaignEntity campaign, CorrectionTriggerType triggerType, 
                                       BigDecimal threshold, BigDecimal observed, Integer cohortSize) {
        log.info("Requesting LLM decision for campaign {} due to {}", campaign.getId(), triggerType);
        
        String prompt = String.format("Campaign %s triggered %s. Threshold was %s, observed %s. Respond ONLY with JSON. No preamble.", 
                campaign.getId(), triggerType, threshold, observed);

        try {
            CorrectionDecisionDto decision = agentLlmGatewayService.callGemini(prompt, CorrectionDecisionDto.class);
            correctionExecutionService.executeCorrection(campaign, triggerType, threshold, observed, cohortSize, decision);
        } catch (Exception e) {
            log.error("Failed to get correction decision from LLM: {}", e.getMessage());
            // Fallback action
            CorrectionDecisionDto fallback = CorrectionDecisionDto.builder()
                    .actionTaken(CorrectionActionType.PAUSE_CAMPAIGN)
                    .aiReasoning("Fallback to pause due to LLM failure: " + e.getMessage())
                    .build();
            correctionExecutionService.executeCorrection(campaign, triggerType, threshold, observed, cohortSize, fallback);
        }
    }
}
