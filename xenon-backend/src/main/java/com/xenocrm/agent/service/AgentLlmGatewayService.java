package com.xenocrm.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * AgentLlmGatewayService — The ONLY gateway to the LLM (Gemini).
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgentLlmGatewayService {

    @Qualifier("geminiRestClient")
    private final RestClient geminiRestClient;

    private final ObjectMapper objectMapper;

    @Value("${gemini.api-key}")
    private String geminiApiKey;

    @Value("${gemini.model}")
    private String geminiModel;

    public <T> T callGemini(String prompt, Class<T> responseType) {
        log.info("Calling Gemini API with model: {}", geminiModel);

        String uri = String.format("/%s:generateContent?key=%s", geminiModel, geminiApiKey);

        Map<String, Object> requestBody = Map.of(
            "contents", Map.of(
                "parts", Map.of("text", prompt)
            )
        );

        try {
            String jsonResponse = geminiRestClient.post()
                    .uri(uri)
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

            // Stub: In reality, parse the response from Gemini structure -> JSON string -> Object
            // Here we just return a new instance for the stub since we don't have a real Gemini response parsing setup
            return responseType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Failed to call Gemini API: {}", e.getMessage());
            throw new RuntimeException("LLM call failed", e);
        }
    }
}
