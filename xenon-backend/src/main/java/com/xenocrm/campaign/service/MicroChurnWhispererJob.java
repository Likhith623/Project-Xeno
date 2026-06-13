package com.xenocrm.campaign.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.customer.entity.CustomerMetricsEntity;
import com.xenocrm.customer.repository.CustomerMetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Phase 8: The Whisperer (Micro-Churn Velocity Engine)
 * Detects microscopic lags in purchasing velocity and autonomously drafts
 * a gentle check-in campaign before the user enters a high-churn-risk state.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MicroChurnWhispererJob {

    private final CustomerMetricsRepository metricsRepository;
    private final AgentLlmGatewayService llmGatewayService;
    private final CampaignRepository campaignRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Runs at 3 AM
    public void detectMicroChurn() {
        log.info("🕵️ Running The Whisperer (Micro-Churn Velocity Engine)...");

        List<CustomerMetricsEntity> laggingUsers = metricsRepository.findMicroChurnSlippingUsers();

        if (laggingUsers.isEmpty()) {
            log.info("No micro-churn velocity lag detected today.");
            return;
        }

        log.info("Detected {} users lagging behind their personal purchase velocity.", laggingUsers.size());

        // We will randomly pick one to create a draft campaign for, as a demonstration.
        CustomerMetricsEntity target = laggingUsers.get(0);
        String customerName = target.getCustomer() != null && target.getCustomer().getName() != null ? target.getCustomer().getName() : "VIP";

        String prompt = "You are the 'Whisperer' AI. This customer, " + customerName + ", usually buys every " + target.getAvgDaysBetweenOrders() + 
                        " days, but it has now been " + target.getRecencyDays() + " days. They are showing a microscopic lag in velocity. " +
                        "Write a 1-sentence, highly empathetic, subtle check-in message. DO NOT try to aggressively sell them anything. " +
                        "Just make them feel seen and valued.";

        String copy = llmGatewayService.callGemini(prompt).trim();
        log.info("Drafted Micro-Churn Copy for {}:\n{}", customerName, copy);

        // Save as a draft campaign
        CampaignEntity campaign = CampaignEntity.builder()
                .name("Micro-Churn Check-in: " + customerName)
                .description("Autonomously created by The Whisperer for velocity lag.")
                .goal("Re-engage lagging velocity users.")
                .status(CampaignStatus.DRAFT)
                .createdByAgent(true)
                .build();
        
        campaignRepository.save(campaign);
        log.info("Saved Whisperer DRAFT Campaign. Ready for marketer Tinder-Swipe approval.");
    }
}
