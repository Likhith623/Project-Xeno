package com.xenocrm.variant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantEvolutionJob {

    private final CampaignRepository campaignRepository;
    private final MessageVariantRepository variantRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    // Runs every hour
    @Scheduled(initialDelay = 120000, fixedRate = 3600000)
    public void executeThompsonSamplingEvolution() {
        log.info("Running Automated A/B/n Content Evolution Job...");
        List<CampaignEntity> activeCampaigns = campaignRepository.findAll();

        for (CampaignEntity campaign : activeCampaigns) {
            if (!"RUNNING".equals(campaign.getStatus().name())) continue;

            List<MessageVariantEntity> activeVariants = variantRepository.findAll().stream()
                .filter(MessageVariantEntity::isMabIsActive)
                .limit(1)
                .toList();

            List<MessageVariantEntity> variants = variantRepository.findAllByCampaignId(campaign.getId());
            MessageVariantEntity winner = null;
            
            // Simplified check: If one variant has significantly more conversions
            for (MessageVariantEntity v : variants) {
                if (v.getMabConversions() > 50 && v.getMabAlpha().doubleValue() > v.getMabBeta().doubleValue() * 1.5) {
                    winner = v;
                    break;
                }
            }

            if (winner != null && variants.size() < 5) {
                log.info("Evolving winning variant {} for campaign {}", winner.getId(), campaign.getId());
                
                String prompt = "You are an AI Evolutionary Marketer. The following variant is winning in a live campaign:\n" +
                                "Subject: " + winner.getSubjectLine() + "\n" +
                                "Body: " + winner.getBodyHtml() + "\n\n" +
                                "Generate 2 NEW mutated variants that iterate on this success (e.g. one shorter, one with different emojis).\n" +
                                "Generate a JSON EXACTLY like this:\n" +
                                "[\n" +
                                "  { \"subjectLine\": \"string\", \"bodyHtml\": \"string\" },\n" +
                                "  { \"subjectLine\": \"string\", \"bodyHtml\": \"string\" }\n" +
                                "]\n" +
                                "Respond ONLY with valid JSON.";
                                
                try {
                    String llmResponse = llmGatewayService.callGemini(prompt);
                    if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
                    if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
                    if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
                    
                    JsonNode jsonNode = objectMapper.readTree(llmResponse.trim());
                    for (JsonNode node : jsonNode) {
                        MessageVariantEntity newVariant = MessageVariantEntity.builder()
                                .campaign(campaign)
                                .name(winner.getName() + " - Evolved")
                                .channel(winner.getChannel())
                                .subjectLine(node.path("subjectLine").asText())
                                .bodyHtml(node.path("bodyHtml").asText())
                                .mabAlpha(java.math.BigDecimal.valueOf(1.0))
                                .mabBeta(java.math.BigDecimal.valueOf(1.0))
                                .mabIsActive(true)
                                .build();
                        variantRepository.save(newVariant);
                    }
                } catch (Exception e) {
                    log.error("Evolution failed for campaign {}", campaign.getId(), e);
                }
            }
        }
    }
}
