package com.xenocrm.campaign.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignAnalyticsService {

    private final CampaignRepository campaignRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    public List<String> getNaturalLanguageAnalytics(UUID campaignId) {
        CampaignEntity campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new IllegalArgumentException("Campaign not found"));

        String metrics = String.format("Sent: %d, Delivered: %d, Opened: %d, Clicked: %d, Converted: %d, Revenue: %s",
                campaign.getTotalSent(), campaign.getTotalDelivered(), campaign.getTotalOpened(),
                campaign.getTotalClicked(), campaign.getTotalConverted(), campaign.getRevenueAttributed());

        String prompt = "You are a Campaign Analyst. The campaign goal was: " + campaign.getGoal() + "\n" +
                        "Here are the final metrics:\n" + metrics + "\n" +
                        "Analyze these metrics and provide exactly 3 brief bullet points explaining the performance, either why it succeeded or failed.\n" +
                        "Generate a JSON response EXACTLY in this format:\n" +
                        "{\n" +
                        "  \"analysis\": [\"Point 1\", \"Point 2\", \"Point 3\"]\n" +
                        "}\n" +
                        "Respond ONLY with valid JSON.";

        try {
            String llmResponse = llmGatewayService.callGemini(prompt);
            
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
            llmResponse = llmResponse.trim();

            JsonNode jsonNode = objectMapper.readTree(llmResponse);
            List<String> analysis = new ArrayList<>();
            jsonNode.path("analysis").forEach(node -> analysis.add(node.asText()));

            return analysis;

        } catch (Exception e) {
            log.error("Analytics generation failed", e);
            List<String> fallback = new ArrayList<>();
            fallback.add("The campaign has generated " + campaign.getTotalConverted() + " conversions and $" + campaign.getRevenueAttributed() + " in revenue.");
            fallback.add("The open rate is " + (campaign.getTotalSent() > 0 ? (campaign.getTotalOpened() * 100 / campaign.getTotalSent()) : 0) + "%.");
            fallback.add("AI Narrative analysis is temporarily unavailable. Check back later.");
            return fallback;
        }
    }
}
