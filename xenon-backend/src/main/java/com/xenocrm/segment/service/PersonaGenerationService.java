package com.xenocrm.segment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaGenerationService {

    private final AudienceSegmentRepository segmentRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final ObjectMapper objectMapper;

    public Map<String, Object> generatePersona(UUID segmentId) {
        AudienceSegmentEntity segment = segmentRepository.findById(segmentId)
                .orElseThrow(() -> new IllegalArgumentException("Segment not found"));

        String filterLogic = segment.getFilterJson() != null ? segment.getFilterJson().toString() : segment.getFilterSql();
        
        String prompt = "You are an AI Persona Generator. Analyze the following audience segment logic:\n" +
                        "Segment Name: " + segment.getName() + "\n" +
                        "Segment Logic: " + filterLogic + "\n\n" +
                        "Generate a detailed Persona profile for the people in this segment.\n" +
                        "Provide a JSON response EXACTLY in this format:\n" +
                        "{\n" +
                        "  \"ageRange\": \"string\",\n" +
                        "  \"preferences\": [\"string\"],\n" +
                        "  \"communicationStyle\": [\"string\"]\n" +
                        "}\n" +
                        "Respond ONLY with valid JSON.";

        try {
            String llmResponse = llmGatewayService.callGemini(prompt);
            
            if (llmResponse.startsWith("```json")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
            llmResponse = llmResponse.trim();

            return objectMapper.readValue(llmResponse, HashMap.class);

        } catch (Exception e) {
            log.error("Persona generation failed", e);
            throw new RuntimeException("Persona generation failed", e);
        }
    }
}
