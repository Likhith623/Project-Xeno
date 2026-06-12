package com.xenocrm.agent.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.dto.AgentChatRequestDto;
import com.xenocrm.agent.dto.AgentChatResponseDto;
import com.xenocrm.agent.entity.AgentDecisionEntity;
import com.xenocrm.agent.entity.AgentSessionEntity;
import com.xenocrm.agent.enums.AgentDecisionType;
import com.xenocrm.agent.enums.AgentSessionStatus;
import com.xenocrm.agent.repository.AgentDecisionRepository;
import com.xenocrm.agent.repository.AgentSessionRepository;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentStatus;
import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xenocrm.channelservice.enums.MessageChannel;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentOrchestrationService {

    private final AgentSessionRepository sessionRepository;
    private final AgentDecisionRepository decisionRepository;
    private final CampaignRepository campaignRepository;
    private final AudienceSegmentRepository segmentRepository;
    private final MessageVariantRepository variantRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    @Transactional
    public AgentChatResponseDto processChat(AgentChatRequestDto requestDto) {
        AgentSessionEntity session = AgentSessionEntity.builder()
                .goal(requestDto.getPrompt())
                .status(AgentSessionStatus.RUNNING)
                .modelUsed("gemini-2.5-pro")
                .conversationLog(new ArrayList<>())
                .plan(new HashMap<>())
                .startedAt(OffsetDateTime.now())
                .build();
        session = sessionRepository.save(session);
        
        log.info("Starting async orchestration for session {}", session.getId());
        // Start async processing
        runAsyncOrchestration(session.getId(), requestDto.getPrompt());

        return AgentChatResponseDto.builder()
                .sessionId(session.getId())
                .textReply("Agent started working on your request. Poll /sessions/" + session.getId() + " for updates.")
                .actionTaken(AgentDecisionType.MEMORY_LOOKUP)
                .build();
    }

    @Async("taskExecutor")
    @Transactional
    public void runAsyncOrchestration(UUID sessionId, String userPrompt) {
        log.info("Async orchestration running for session {}", sessionId);
        try {
            AgentSessionEntity session = sessionRepository.findById(sessionId).orElseThrow();
            
            // Step 1: Reason and Plan
            saveDecision(sessionId, 1, AgentDecisionType.MEMORY_LOOKUP, "User requested: " + userPrompt, "Querying memory and deciding plan", "Need to create segment, campaign, and 3 beautiful HTML variants.");

            // Prompt Gemini for JSON output
            String prompt = "You are a Sovereign Marketing AI. The user requested: " + userPrompt + "\n" +
                            "Generate a JSON object with the following schema exactly:\n" +
                            "{\n" +
                            "  \"segmentName\": \"string\",\n" +
                            "  \"segmentSql\": \"string (e.g. SELECT id FROM customers WHERE monetary_total > 500)\",\n" +
                            "  \"campaignName\": \"string\",\n" +
                            "  \"variants\": [\n" +
                            "    { \"channel\": \"EMAIL\", \"subjectLine\": \"string\", \"bodyHtml\": \"string containing FULL inline CSS, vibrant gradients, a modern banner, clean typography, and a very beautiful CTA button\" }\n" +
                            "  ]\n" +
                            "}\n" +
                            "Generate exactly 3 extremely beautiful variants. Respond ONLY with valid JSON. No markdown backticks.";
            
            saveDecision(sessionId, 2, AgentDecisionType.VARIANT_GENERATION, prompt, "Calling Gemini", "Using Gemini to generate SQL and HTML variants.");

            String llmResponse = llmGatewayService.callGemini(prompt);
            
            // Clean up backticks if any
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
            llmResponse = llmResponse.trim();
            
            JsonNode jsonNode = objectMapper.readTree(llmResponse);
            
            // 1. Create Segment
            AudienceSegmentEntity segment = AudienceSegmentEntity.builder()
                .name(jsonNode.get("segmentName").asText())
                .description("AI Generated Segment")
                .type(SegmentType.DYNAMIC)
                .filterSql(jsonNode.get("segmentSql").asText())
                .status(SegmentStatus.DRAFT)
                .agentGoal(userPrompt != null && userPrompt.length() > 255 ? userPrompt.substring(0, 250) + "..." : userPrompt)
                .createdByAgent(true)
                .build();
            segment = segmentRepository.save(segment);

            // 2. Create Campaign
            CampaignEntity campaign = CampaignEntity.builder()
                .name(jsonNode.get("campaignName").asText())
                .description("AI Generated Campaign")
                .goal(userPrompt != null && userPrompt.length() > 255 ? userPrompt.substring(0, 250) + "..." : userPrompt)
                .status(CampaignStatus.DRAFT)
                .createdByAgent(true)
                .targetSegment(segment)
                .agentSessionId(sessionId.toString())
                .build();
            campaign = campaignRepository.save(campaign);

            // 3. Create Variants
            JsonNode variantsNode = jsonNode.get("variants");
            for (JsonNode vNode : variantsNode) {
                MessageVariantEntity variant = MessageVariantEntity.builder()
                    .campaign(campaign)
                    .name("AI Variant")
                    .channel(MessageChannel.valueOf(vNode.get("channel").asText().toLowerCase()))
                    .subjectLine(vNode.get("subjectLine").asText())
                    .bodyHtml(vNode.get("bodyHtml").asText())
                    .generatedByAi(true)
                    .generationPrompt(userPrompt != null && userPrompt.length() > 255 ? userPrompt.substring(0, 250) + "..." : userPrompt)
                    .mabAlpha(BigDecimal.ONE)
                    .mabBeta(BigDecimal.ONE)
                    .build();
                variantRepository.save(variant);
            }

            saveDecision(sessionId, 3, AgentDecisionType.SEGMENT_QUERY, "Parsed JSON", "Saved Segment, Campaign, and Variants", "Awaiting human approval to execute.");

            // Update session
            session.setCreatedSegmentId(segment.getId());
            session.setCreatedCampaignId(campaign.getId());
            session.setStatus(AgentSessionStatus.COMPLETED);
            session.setCompletedAt(OffsetDateTime.now());
            sessionRepository.save(session);
            
            log.info("Async orchestration complete for session {}", sessionId);
        } catch (Exception e) {
            log.error("Async Agent failed", e);
            AgentSessionEntity session = sessionRepository.findById(sessionId).orElseThrow();
            session.setStatus(AgentSessionStatus.FAILED);
            session.setErrorMessage(e.getMessage());
            session.setCompletedAt(OffsetDateTime.now());
            sessionRepository.save(session);
        }
    }

    private void saveDecision(UUID sessionId, int step, AgentDecisionType type, String input, String output, String reason) {
        AgentDecisionEntity decision = AgentDecisionEntity.builder()
            .session(sessionRepository.findById(sessionId).orElse(null))
            .stepNumber(step)
            .decisionType(type)
            .inputContext(Map.of("text", input))
            .outputAction(Map.of("action", output))
            .reasoning(reason)
            .build();
        decisionRepository.save(decision);
    }
}
