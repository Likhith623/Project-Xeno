package com.xenocrm.simulator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.simulator.entity.SimulationRunEntity;
import com.xenocrm.simulator.enums.SimulationRunStatus;
import com.xenocrm.simulator.repository.SimulationRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SimulationService {

    private final SimulationRunRepository simulationRunRepository;
    private final CampaignRepository campaignRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    public SimulationRunEntity runCounterfactualSimulation(UUID campaignId, String channel) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        String prompt = "You are an AI Audience Simulator. We are planning a campaign targeting segment ID: " + campaign.getTargetSegment().getId() + ".\n" +
                        "The goal of the campaign is: " + campaign.getGoal() + "\n" +
                        "What are the predicted results if we send this via " + channel + " instead of the default channel?\n" +
                        "Generate a JSON response EXACTLY in this format:\n" +
                        "{\n" +
                        "  \"predictedOpenRate\": 0.45,\n" +
                        "  \"predictedCtr\": 0.05,\n" +
                        "  \"predictedConversionRate\": 0.02,\n" +
                        "  \"predictedRevenue\": 37000.00,\n" +
                        "  \"reasoning\": \"Email has lower CTR but higher reach for this segment.\"\n" +
                        "}\n" +
                        "Respond ONLY with valid JSON. No markdown backticks.";

        try {
            String llmResponse = llmGatewayService.callGemini(prompt);
            
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
            llmResponse = llmResponse.trim();

            JsonNode jsonNode = objectMapper.readTree(llmResponse);

            SimulationRunEntity run = SimulationRunEntity.builder()
                    .campaign(campaign)
                    .status(SimulationRunStatus.COMPLETED)
                    .syntheticAudienceSize(500)
                    .predictedOpenRate(new BigDecimal(jsonNode.path("predictedOpenRate").asText("0.0")))
                    .predictedCtr(new BigDecimal(jsonNode.path("predictedCtr").asText("0.0")))
                    .predictedConversionRate(new BigDecimal(jsonNode.path("predictedConversionRate").asText("0.0")))
                    .predictedRevenue(new BigDecimal(jsonNode.path("predictedRevenue").asText("0.0")))
                    .startedAt(OffsetDateTime.now())
                    .completedAt(OffsetDateTime.now())
                    .build();

            return simulationRunRepository.save(run);

        } catch (Exception e) {
            log.error("Simulation failed", e);
            throw new RuntimeException("Simulation failed", e);
        }
    }
}
