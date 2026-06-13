package com.xenocrm.campaign.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.channelservice.enums.MessageChannel;
import com.xenocrm.product.entity.ProductEntity;
import com.xenocrm.product.repository.ProductRepository;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictiveInventoryJob {

    private final ProductRepository productRepository;
    private final AudienceSegmentRepository segmentRepository;
    private final CampaignRepository campaignRepository;
    private final MessageVariantRepository variantRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    /**
     * Runs weekly on Sunday at 1 AM.
     * Detects products with > 100 inventory and older than 90 days.
     */
    @Scheduled(cron = "0 0 1 * * SUN")
    public void executePredictiveInventoryClearance() {
        log.info("Running Predictive Inventory Clearance job...");

        OffsetDateTime ninetyDaysAgo = OffsetDateTime.now().minusDays(90);
        List<ProductEntity> deadStock = productRepository.findByInventoryCountGreaterThanAndCreatedAtBefore(100, ninetyDaysAgo);

        if (deadStock.isEmpty()) {
            log.info("No dead stock detected. Inventory is healthy.");
            return;
        }

        for (ProductEntity product : deadStock) {
            log.info("Detected dead stock: {} (Inventory: {})", product.getName(), product.getInventoryCount());

            // 1. Generate an Audience Segment for people who might buy this
            // We'll target users who previously bought from the same category
            String sqlFilter = "SELECT DISTINCT c.id FROM customers c " +
                    "JOIN orders o ON c.id = o.customer_id " +
                    "JOIN order_items oi ON o.id = oi.order_id " +
                    "JOIN products p ON oi.product_id = p.id " +
                    "WHERE p.category_id = '" + (product.getCategory() != null ? product.getCategory().getId() : "") + "' " +
                    "AND c.is_globally_opted_out = false";

            AudienceSegmentEntity segment = AudienceSegmentEntity.builder()
                    .name("Predictive Clearance: " + product.getName() + " Buyers")
                    .description("Autonomously generated segment to liquidate dead stock of " + product.getName())
                    .type(SegmentType.DYNAMIC)
                    .filterSql(sqlFilter)
                    .build();
            segment = segmentRepository.save(segment);

            // 2. Draft the Campaign
            CampaignEntity campaign = CampaignEntity.builder()
                    .name("Clearance Sale: " + product.getName())
                    .description("Autonomous clearance campaign to liquidate " + product.getInventoryCount() + " units of " + product.getName())
                    .goal("Sell out dead stock immediately")
                    .targetSegment(segment)
                    .status(CampaignStatus.DRAFT) // Human-in-the-loop review needed
                    .build();
            campaign = campaignRepository.save(campaign);

            // 3. Autonomously draft highly targeted variants via LLM
            String prompt = "You are an expert e-commerce copywriter. We have excess inventory (" + product.getInventoryCount() + " units) of '" + product.getName() + "' priced at " + product.getCurrency() + " " + product.getPrice() + ". " +
                    "Write a short, urgent clearance sale email to customers who previously bought similar items. " +
                    "Offer a 30% discount.\n" +
                    "Respond with JSON exactly like this:\n" +
                    "{ \"subjectLine\": \"string\", \"bodyHtml\": \"string\" }";

            try {
                String llmResponse = llmGatewayService.callGemini(prompt);
                if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
                if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
                if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);

                JsonNode jsonNode = objectMapper.readTree(llmResponse.trim());
                MessageVariantEntity variant = MessageVariantEntity.builder()
                        .campaign(campaign)
                        .name("Clearance Email Variant A")
                        .channel(MessageChannel.email)
                        .subjectLine(jsonNode.path("subjectLine").asText())
                        .bodyHtml(jsonNode.path("bodyHtml").asText())
                        .mabAlpha(java.math.BigDecimal.valueOf(1.0))
                        .mabBeta(java.math.BigDecimal.valueOf(1.0))
                        .mabIsActive(true)
                        .generatedByAi(true)
                        .build();
                variantRepository.save(variant);
                log.info("Successfully drafted clearance campaign for {}", product.getName());

            } catch (Exception e) {
                log.error("Failed to generate clearance variant for {}", product.getName(), e);
            }
        }
    }
}
