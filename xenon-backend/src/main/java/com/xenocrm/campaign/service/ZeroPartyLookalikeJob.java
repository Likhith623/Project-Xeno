package com.xenocrm.campaign.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import com.xenocrm.customer.repository.CustomerMetricsRepository;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZeroPartyLookalikeJob {

    private final CustomerMetricsRepository metricsRepository;
    private final AudienceSegmentRepository segmentRepository;
    private final AgentLlmGatewayService llmGatewayService;

    /**
     * Runs on the 1st of every month at 3 AM.
     */
    @Scheduled(cron = "0 0 3 1 * ?")
    public void executeLookalikeSynthesis() {
        log.info("Running Zero-Party Lookalike Synthesis job...");

        List<CustomerMetricsEntity> top100Vips = metricsRepository.findTop100ByOrderByRfmScoreDesc();
        if (top100Vips.isEmpty()) {
            return;
        }

        // Aggregate trait summary
        String vipSummary = top100Vips.stream()
                .map(m -> {
                    String city = m.getCustomer().getCity() != null ? m.getCustomer().getCity() : "Unknown";
                    String channel = m.getCustomer().getPreferredChannel() != null ? m.getCustomer().getPreferredChannel().name() : "Unknown";
                    String attrs = m.getCustomer().getCustomAttributes() != null ? m.getCustomer().getCustomAttributes().toString() : "{}";
                    return "City: " + city + ", Channel: " + channel + ", Attributes: " + attrs;
                })
                .collect(Collectors.joining("\n"));

        List<String> vipIds = top100Vips.stream().map(m -> "'" + m.getCustomer().getId().toString() + "'").toList();
        String excludeIds = String.join(",", vipIds);

        String prompt = "You are an AI Data Scientist doing Lookalike Audience Synthesis. " +
                "Here is a summary of our top 100 VIP customers:\n" +
                vipSummary + "\n\n" +
                "Based on the hidden behavioral traits (common cities, attributes) you observe above, write a purely valid PostgreSQL WHERE clause " +
                "that would select similar 'Lookalike' customers from the 'customers' table who match these traits. " +
                "Ensure you exclude the actual VIPs by adding: AND id NOT IN (" + excludeIds + ").\n" +
                "Return ONLY the raw SQL WHERE clause without the word 'WHERE', and no markdown backticks.";

        try {
            String generatedSql = llmGatewayService.callGemini(prompt).trim();
            if (generatedSql.startsWith("```sql")) generatedSql = generatedSql.substring(6);
            if (generatedSql.startsWith("```")) generatedSql = generatedSql.substring(3);
            if (generatedSql.endsWith("```")) generatedSql = generatedSql.substring(0, generatedSql.length() - 3);

            String fullQuery = "SELECT id FROM customers WHERE " + generatedSql;

            AudienceSegmentEntity segment = AudienceSegmentEntity.builder()
                    .name("Ghost Audience: Lookalikes of VIPs")
                    .description("Autonomously synthesized AI segment discovering hidden trait correlations of the top 1% LTV users.")
                    .type(SegmentType.DYNAMIC)
                    .filterSql(fullQuery)
                    .build();

            segmentRepository.save(segment);
            log.info("Successfully synthesized Zero-Party Lookalike segment.");

        } catch (Exception e) {
            log.error("Failed to generate lookalike segment", e);
        }
    }
}
