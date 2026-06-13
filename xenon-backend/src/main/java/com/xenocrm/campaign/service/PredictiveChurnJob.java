package com.xenocrm.campaign.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictiveChurnJob {

    private final CustomerRepository customerRepository;
    private final AudienceSegmentRepository segmentRepository;
    private final CampaignRepository campaignRepository;
    private final MessageVariantRepository variantRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    // Runs once a day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void interceptChurningCustomers() {
        log.info("Running Predictive Churn Interception Job...");

        // Fetch customers with high churn risk
        List<CustomerEntity> churningCustomers = customerRepository.findAll().stream()
                .filter(c -> c.getMetrics() != null && c.getMetrics().getChurnProbability() != null && c.getMetrics().getChurnProbability().doubleValue() > 0.7)
                .toList();

        if (churningCustomers.isEmpty()) {
            log.info("No high-risk churning customers found today.");
            return;
        }

        log.info("Detected {} high-risk churning customers. Drafting Win-Back Campaign.", churningCustomers.size());

        try {
            // 1. Create a dynamic segment for these users
            String segmentQuery = "SELECT id, email, phone FROM customers c JOIN customer_metrics m ON c.id = m.customer_id WHERE m.churn_probability > 0.7";
            AudienceSegmentEntity segment = AudienceSegmentEntity.builder()
                    .name("Auto-Detected Churn Risk (Prob > 70%)")
                    .description("Autonomously created by the CRM to catch churning users")
                    .type(SegmentType.DYNAMIC)
                    .filterSql(segmentQuery)
                    .build();
            segment = segmentRepository.save(segment);

            // 2. Draft the Campaign
            CampaignEntity campaign = CampaignEntity.builder()
                    .name("Proactive Win-Back (Autopilot)")
                    .goal("Prevent high-value churn")
                    .targetSegment(segment)
                    .status(CampaignStatus.DRAFT) // Leaves it for Human-in-the-Loop swipe approval
                    .build();
            campaign = campaignRepository.save(campaign);

            // 3. Generate Win-Back Variants via LLM
            String prompt = "You are an AI Retention Strategist. We have a segment of customers with >70% churn probability.\n" +
                            "Generate 1 highly effective win-back email variant with a 15% discount offer to save them.\n" +
                            "Generate a JSON EXACTLY like this:\n" +
                            "{\n" +
                            "  \"subjectLine\": \"We miss you! Here is 15% off.\",\n" +
                            "  \"bodyHtml\": \"<h1>Come back!</h1>\"\n" +
                            "}\n" +
                            "Respond ONLY with valid JSON.";

            String llmResponse = llmGatewayService.callGemini(prompt);
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);

            JsonNode jsonNode = objectMapper.readTree(llmResponse.trim());

            MessageVariantEntity variant = MessageVariantEntity.builder()
                    .campaign(campaign)
                    .name("Win-Back Offer 15%")
                    .channel(MessageChannel.email)
                    .subjectLine(jsonNode.path("subjectLine").asText())
                    .bodyHtml(jsonNode.path("bodyHtml").asText())
                    .mabAlpha(java.math.BigDecimal.valueOf(1.0))
                    .mabBeta(java.math.BigDecimal.valueOf(1.0))
                    .mabIsActive(true)
                    .build();
            variantRepository.save(variant);

            log.info("Win-Back campaign successfully drafted and awaits human approval.");

        } catch (Exception e) {
            log.error("Failed to draft Predictive Churn campaign", e);
        }
    }
}
