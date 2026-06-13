package com.xenocrm.test.controller;

import com.xenocrm.campaign.service.CampaignFundManagerJob;
import com.xenocrm.campaign.service.ChannelFatigueJob;
import com.xenocrm.campaign.service.MicroChurnWhispererJob;
import com.xenocrm.campaign.service.MultiAgentDebateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller strictly for AGI Real-Time Testing and Verification.
 * Manually triggers the nightly/hourly cron jobs.
 */
@RestController
@RequestMapping("/api/v1/test/agi")
@RequiredArgsConstructor
@Slf4j
public class AITestingController {

    private final MultiAgentDebateService warRoomService;
    private final CampaignFundManagerJob fundManagerJob;
    private final ChannelFatigueJob fatigueJob;
    private final MicroChurnWhispererJob whispererJob;

    @PostMapping("/trigger-war-room")
    public String triggerWarRoom(@RequestBody Map<String, String> payload) {
        String goal = payload.getOrDefault("goal", "Win back churned winter buyers with maximum urgency.");
        log.info("🧪 Real-Time Test: Triggering The War Room for goal: {}", goal);
        return warRoomService.orchestrateDebate(goal);
    }

    @PostMapping("/trigger-fund-manager")
    public String triggerFundManager() {
        log.info("🧪 Real-Time Test: Triggering The Fund Manager");
        fundManagerJob.optimizeBudgets();
        return "Fund Manager Execution Complete. Check server logs for arbitrage details.";
    }

    @PostMapping("/trigger-omni-awareness")
    public String triggerOmniAwareness() {
        log.info("🧪 Real-Time Test: Triggering Omni-Awareness (Sleep Agent & Whisperer)");
        fatigueJob.executeFatigueCheck();
        whispererJob.detectMicroChurn();
        return "Omni-Awareness Execution Complete. Check server logs for fatigue and micro-churn detections.";
    }
}
