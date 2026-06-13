package com.xenocrm.campaign.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.channelservice.service.SlackNotificationService;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import com.xenocrm.customer.repository.CustomerMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VipConciergeEscalationJob {

    private final CustomerMetricsRepository metricsRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final SlackNotificationService slackNotificationService;

    /**
     * Runs nightly at 2 AM.
     * Looks for Whales (LTV > $5000) who haven't ordered in > 180 days.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void executeVipConciergeEscalation() {
        log.info("Running VIP Concierge Escalation (The Butler)...");

        BigDecimal minLtv = new BigDecimal("5000.00");
        Integer minRecencyDays = 180;

        List<CustomerMetricsEntity> slippingWhales = metricsRepository.findByMonetaryTotalGreaterThanAndRecencyDaysGreaterThan(minLtv, minRecencyDays);

        if (slippingWhales.isEmpty()) {
            log.info("No slipping VIPs detected tonight.");
            return;
        }

        for (CustomerMetricsEntity whale : slippingWhales) {
            String name = whale.getCustomer().getName() != null ? whale.getCustomer().getName() : "VIP Customer";
            String attrs = whale.getCustomer().getCustomAttributes() != null ? whale.getCustomer().getCustomAttributes().toString() : "None";
            String ltv = whale.getMonetaryTotal() != null ? whale.getMonetaryTotal().toString() : "5000+";
            String recency = whale.getRecencyDays() != null ? whale.getRecencyDays().toString() : "180+";

            log.info("Detected slipping VIP: {} (LTV: ${}, Idle for: {} days)", name, ltv, recency);

            String prompt = "You are a high-end luxury concierge sales assistant. Your VIP customer '" + name + "' " +
                    "(Lifetime Value: $" + ltv + ") hasn't purchased in " + recency + " days. " +
                    "They have the following profile attributes: " + attrs + ". " +
                    "Write a short, highly personalized, empathetic script for a human sales rep to use when calling them on the phone tomorrow to check in and win them back. " +
                    "Do not sound like a robot; sound like a high-end luxury brand ambassador. Just provide the script.";

            try {
                String script = llmGatewayService.callGemini(prompt).trim();
                
                // Fire the Slack webhook to the sales team
                slackNotificationService.sendVipEscalationAlert(name, script);
            } catch (Exception e) {
                log.error("Failed to escalate VIP {}", name, e);
            }
        }
    }
}
