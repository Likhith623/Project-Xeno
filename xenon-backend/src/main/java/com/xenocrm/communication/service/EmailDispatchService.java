package com.xenocrm.communication.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailDispatchService — Handles sending actual emails via Resend HTTP API.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailDispatchService {

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from-email:onboarding@resend.dev}")
    private String fromEmail;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Sends an HTML email using Resend HTTP API.
     *
     * @param to          The recipient's email address
     * @param subject     The subject of the email
     * @param htmlContent The HTML body of the email
     */
    public void sendEmail(String to, String subject, String htmlContent) {
        log.info("Sending email via Resend to {} with subject: {}", to, subject);
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("from", "Xeno CRM <" + fromEmail + ">");
            payload.put("to", List.of(to));
            payload.put("subject", subject);
            payload.put("html", htmlContent);

            String jsonPayload = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("Successfully sent email via Resend to {}. Response: {}", to, response.body());
            } else {
                log.error("Failed to send email via Resend to {}. Status: {}, Response: {}", to, response.statusCode(), response.body());
                throw new RuntimeException("Resend API failed: " + response.body());
            }

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
