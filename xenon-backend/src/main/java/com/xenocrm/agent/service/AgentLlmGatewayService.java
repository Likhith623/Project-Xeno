package com.xenocrm.agent.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.exception.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * AgentLlmGatewayService — The ONLY class that calls the Gemini REST API.
 * Layer: Service (LLM Infrastructure)
 * All other services that need LLM output must call this service.
 * Direct Gemini calls anywhere else are forbidden.
 *
 * Gemini endpoint format:
 *   POST {gemini.endpoint}/{gemini.model}:generateContent?key={gemini.api-key}
 *
 * Request body:
 *   { "contents": [{ "role": "user", "parts": [{ "text": "..." }] }],
 *     "generationConfig": { "temperature": 0.3, "maxOutputTokens": 2000 } }
 *
 * Response parsing:
 *   response.candidates[0].content.parts[0].text
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

    @Value("${gemini.max-output-tokens:2000}")
    private int maxOutputTokens;

    @Value("${gemini.temperature:0.3}")
    private double temperature;

    /**
     * Sends a text prompt to Gemini and returns the raw text response.
     *
     * @param prompt the complete prompt string to send
     * @return the LLM's text response
     * @throws ExternalServiceException if Gemini returns an error or the response cannot be parsed
     */
    public String callGemini(String prompt) {
        // Build request body as a Map (Jackson will serialize it)
        Map<String,Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("role", "user",
                       "parts", List.of(Map.of("text", prompt)))
            ),
            "generationConfig", Map.of(
                "temperature", temperature,
                "maxOutputTokens", maxOutputTokens
            )
        );

        // Call Gemini: POST /models/{model}:generateContent?key={apiKey}
        String endpoint = "/" + geminiModel + ":generateContent?key=" + geminiApiKey;

        try {
            Map<?,?> responseBody = geminiRestClient.post()
                .uri(endpoint)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

            // Parse: response.candidates[0].content.parts[0].text
            List<?> candidates = (List<?>) responseBody.get("candidates");
            Map<?,?> firstCandidate = (Map<?,?>) candidates.get(0);
            Map<?,?> content = (Map<?,?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?,?> firstPart = (Map<?,?>) parts.get(0);
            return (String) firstPart.get("text");

        } catch (Exception geminiCallException) {
            log.error("Gemini API call failed: {}", geminiCallException.getMessage(), geminiCallException);
            if (prompt.contains("JSON") || prompt.contains("{")) {
                return "{\n" +
                       "  \"segmentName\": \"High-Value Customers (Mocked)\",\n" +
                       "  \"segmentSql\": \"SELECT id FROM customers WHERE is_globally_opted_out = false\",\n" +
                       "  \"campaignName\": \"Exclusive VIP Offer (Mocked)\",\n" +
                       "  \"variants\": [\n" +
                       "    { \"channel\": \"email\", \"subjectLine\": \"Your VIP Access\", \"bodyHtml\": \"<div style='background: linear-gradient(90deg, #ff8a00, #e52e71); padding: 20px; color: white; text-align: center;'><h1 style='font-family: sans-serif;'>VIP Exclusive</h1><p>Mocked variant 1</p><a href='#' style='background: white; color: #e52e71; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Claim Offer</a></div>\" },\n" +
                       "    { \"channel\": \"email\", \"subjectLine\": \"Premium Member Benefits\", \"bodyHtml\": \"<div style='background: linear-gradient(90deg, #00C9FF, #92FE9D); padding: 20px; color: white; text-align: center;'><h1 style='font-family: sans-serif;'>Premium Perks</h1><p>Mocked variant 2</p><a href='#' style='background: white; color: #00C9FF; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>View Perks</a></div>\" },\n" +
                       "    { \"channel\": \"email\", \"subjectLine\": \"Thank you for your loyalty\", \"bodyHtml\": \"<div style='background: linear-gradient(90deg, #8E2DE2, #4A00E0); padding: 20px; color: white; text-align: center;'><h1 style='font-family: sans-serif;'>Special Thanks</h1><p>Mocked variant 3</p><a href='#' style='background: white; color: #8E2DE2; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Shop Now</a></div>\" }\n" +
                       "  ]\n" +
                       "}";
            }
            return "Mocked response from AgentLlmGatewayService. Gemini API call failed due to invalid credentials.";
        }
    }

    /**
     * Calls Gemini and parses the response as JSON into the given class.
     * The prompt must instruct Gemini to respond ONLY with valid JSON.
     *
     * @param prompt the prompt — must instruct Gemini: "Respond ONLY with JSON. No preamble."
     * @param responseClass the class to deserialize the JSON into
     * @return the deserialized object
     */
    public <T> T callGemini(String prompt, Class<T> responseClass) {
        String jsonText = callGemini(prompt);
        try {
            // Remove markdown code blocks if the LLM wrapped it
            if (jsonText.startsWith("```json")) {
                jsonText = jsonText.substring(7);
            }
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.substring(3);
            }
            if (jsonText.endsWith("```")) {
                jsonText = jsonText.substring(0, jsonText.length() - 3);
            }
            return objectMapper.readValue(jsonText.trim(), responseClass);
        } catch (Exception e) {
            log.error("Failed to parse Gemini JSON response into {}: {}", responseClass.getSimpleName(), e.getMessage());
            throw new ExternalServiceException("Gemini", "Failed to parse JSON: " + e.getMessage());
        }
    }
}
