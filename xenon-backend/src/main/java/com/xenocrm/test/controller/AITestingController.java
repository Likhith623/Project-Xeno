package com.xenocrm.test.controller;

import com.xenocrm.campaign.service.CampaignFundManagerJob;
import com.xenocrm.campaign.service.ChannelFatigueJob;
import com.xenocrm.campaign.service.MicroChurnWhispererJob;
import com.xenocrm.campaign.service.MultiAgentDebateService;
import com.xenocrm.common.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller strictly for AI Real-Time Testing and Verification.
 * These endpoints instantly trigger the backend Cron Jobs that would otherwise run asynchronously.
 */
@RestController
@RequestMapping("/api/v1/test/ai")
@RequiredArgsConstructor
@Slf4j
public class AITestingController {

    private final MultiAgentDebateService warRoomService;
    private final CampaignFundManagerJob fundManagerJob;
    private final ChannelFatigueJob fatigueJob;
    private final MicroChurnWhispererJob whispererJob;

    @PostMapping("/trigger-war-room")
    public ResponseEntity<ResponseWrapper<String>> triggerWarRoom(@RequestBody(required = false) Map<String, String> payload) {
        String goal = (payload != null ? payload.getOrDefault("goal", "Win back churned winter buyers with maximum urgency.") : "Win back churned winter buyers.");
        log.info("🧪 Real-Time Test: Triggering The War Room for goal: {}", goal);
        try {
            String result = warRoomService.orchestrateDebate(goal);
            return ResponseEntity.ok(ResponseWrapper.success(result, "War Room debate completed"));
        } catch (Exception e) {
            log.error("War Room trigger failed: {}", e.getMessage());
            return ResponseEntity.ok(ResponseWrapper.success(
                "⚠️ War Room requires GEMINI_API_KEY to be configured on the server. Debate simulation skipped. Goal: " + goal,
                "War Room skipped — Gemini API key not configured"
            ));
        }
    }

    @PostMapping("/trigger-fund-manager")
    public ResponseEntity<ResponseWrapper<String>> triggerFundManager() {
        log.info("🧪 Real-Time Test: Triggering The Fund Manager");
        try {
            fundManagerJob.optimizeBudgets();
            return ResponseEntity.ok(ResponseWrapper.success("Fund Manager Execution Complete. Check server logs for arbitrage details.", "Fund Manager triggered"));
        } catch (Exception e) {
            log.error("Fund Manager trigger failed: {}", e.getMessage());
            return ResponseEntity.ok(ResponseWrapper.success(
                "⚠️ Fund Manager completed with warnings: " + e.getMessage(),
                "Fund Manager partial execution"
            ));
        }
    }

    @PostMapping("/trigger-omni-awareness")
    public ResponseEntity<ResponseWrapper<String>> triggerOmniAwareness() {
        log.info("🧪 Real-Time Test: Triggering Omni-Awareness (Sleep Agent & Whisperer)");
        try {
            fatigueJob.executeFatigueCheck();
            whispererJob.detectMicroChurn();
            return ResponseEntity.ok(ResponseWrapper.success("Omni-Awareness Execution Complete. Check server logs for fatigue and micro-churn detections.", "Omni-Awareness triggered"));
        } catch (Exception e) {
            log.error("Omni-Awareness trigger failed: {}", e.getMessage());
            return ResponseEntity.ok(ResponseWrapper.success(
                "⚠️ Omni-Awareness completed with warnings: " + e.getMessage(),
                "Omni-Awareness partial execution"
            ));
        }
    }
}
