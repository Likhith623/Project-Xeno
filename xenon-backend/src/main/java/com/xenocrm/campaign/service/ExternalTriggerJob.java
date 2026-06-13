package com.xenocrm.campaign.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import com.xenocrm.channelservice.enums.MessageChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalTriggerJob {

    private final AudienceSegmentRepository segmentRepository;
    private final CampaignRepository campaignRepository;
    private final MessageVariantRepository variantRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    // Simulates a daily check of external APIs (e.g. Weather, Twitter Trends) at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void generateTriggerCampaign() {
        log.info("Running External Trigger Detection...");

        // In a real system, we would call a Weather API here. For this demo, we simulate a rainy day.
        String currentEvent = "Heavy Rain in the City";

        try {
            // 1. Create a dynamic segment for this
            String segmentQuery = "SELECT id, email, phone FROM customers c WHERE c.tags @> ARRAY['local']::varchar[]";
            AudienceSegmentEntity segment = AudienceSegmentEntity.builder()
                    .name("Local Customers (Triggered by: " + currentEvent + ")")
                    .description("Autonomously created by the CRM based on external events")
                    .type(SegmentType.DYNAMIC)
                    .filterSql(segmentQuery)
                    .build();
            segment = segmentRepository.save(segment);

            // 2. Draft the Campaign
            CampaignEntity campaign = CampaignEntity.builder()
                    .name("News-jack: " + currentEvent)
                    .goal("Drive sales during bad weather")
                    .targetSegment(segment)
                    .status(CampaignStatus.DRAFT) // Leaves it for Human-in-the-Loop swipe approval
                    .build();
            campaign = campaignRepository.save(campaign);

            // 3. Generate Variant via LLM
            String prompt = "You are an AI Opportunistic Marketer. The current external event is: " + currentEvent + ".\n" +
                            "Generate 1 highly contextual email variant to drive sales right now based on this event.\n" +
                            "Generate a JSON EXACTLY like this:\n" +
                            "{\n" +
                            "  \"subjectLine\": \"Stay dry! Here's a rainy day treat 🌧️\",\n" +
                            "  \"bodyHtml\": \"<h1>It's raining!</h1>\"\n" +
                            "}\n" +
                            "Respond ONLY with valid JSON.";

            String llmResponse = llmGatewayService.callGemini(prompt);
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);

            JsonNode jsonNode = objectMapper.readTree(llmResponse.trim());

            MessageVariantEntity variant = MessageVariantEntity.builder()
                    .campaign(campaign)
                    .name("Contextual Weather Offer")
                    .channel(MessageChannel.email)
                    .subjectLine(jsonNode.path("subjectLine").asText())
                    .bodyHtml(jsonNode.path("bodyHtml").asText())
                    .mabAlpha(java.math.BigDecimal.valueOf(1.0))
                    .mabBeta(java.math.BigDecimal.valueOf(1.0))
                    .mabIsActive(true)
                    .build();
            variantRepository.save(variant);

            log.info("External Trigger campaign successfully drafted and awaits human approval.");

        } catch (Exception e) {
            log.error("Failed to draft External Trigger campaign", e);
        }
    }
}
