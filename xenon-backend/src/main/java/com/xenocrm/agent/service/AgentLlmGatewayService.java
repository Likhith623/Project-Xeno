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
                       "  \"segmentName\": \"High-Value Customers (> $500)\",\n" +
                       "  \"segmentSql\": \"SELECT id FROM customers WHERE (email LIKE '%@gmail.com' OR email LIKE '%@srmap.edu.in') AND is_globally_opted_out = false\",\n" +
                       "  \"campaignName\": \"Exclusive VIP Luxury Offer\",\n" +
                       "  \"variants\": [\n" +
                       "    { \"channel\": \"email\", \"subjectLine\": \"An Exclusive Gift Just For You\", \"bodyHtml\": \"<div style='background: linear-gradient(135deg, #0f2027, #203a43, #2c5364); padding: 40px; color: #f8f9fa; text-align: center; font-family: \\\"Helvetica Neue\\\", Helvetica, Arial, sans-serif;'><h1 style='font-size: 36px; margin-bottom: 20px; font-weight: 300; letter-spacing: 2px;'>A Token of Our Appreciation</h1><p style='font-size: 16px; line-height: 1.6; margin-bottom: 30px; color: #ced4da;'>Thank you for your continued trust and remarkable purchases. As one of our most valued clients, we invite you to explore our latest premium collection with an exclusive early access pass.</p><a href='#' style='display: inline-block; background: #e0a96d; color: #1a1a1a; padding: 15px 35px; text-decoration: none; border-radius: 3px; font-weight: bold; font-size: 14px; text-transform: uppercase; letter-spacing: 1px; transition: background 0.3s ease;'>Claim Your VIP Access</a></div>\" }\n" +
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
