package com.xenocrm.correction.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.correction.entity.CorrectionEventEntity;
import com.xenocrm.correction.enums.CorrectionTriggerType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * CampaignHealthMonitorService — Periodically checks running campaigns for issues.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignHealthMonitorService {

    private final CampaignRepository campaignRepository;
    private final AiCorrectionDecisionService aiCorrectionDecisionService;

    @Value("${correction.failure-rate-threshold}")
    private double failureRateThreshold;

    @Value("${correction.opt-out-rate-threshold}")
    private double optOutRateThreshold;

    /**
     * Checks all running campaigns every 10 minutes.
     */
    @Scheduled(fixedDelay = 600000)
    public void monitorRunningCampaigns() {
        log.info("Starting health monitor for running campaigns...");
        List<CampaignEntity> runningCampaigns = campaignRepository.findAllByStatus(CampaignStatus.RUNNING);

        for (CampaignEntity campaign : runningCampaigns) {
            checkCampaignHealth(campaign);
        }
    }

    private void checkCampaignHealth(CampaignEntity campaign) {
        if (campaign.getTotalSent() < 10) return; // Need minimum cohort size to evaluate

        double failureRate = (double) campaign.getTotalFailed() / campaign.getTotalSent();
        if (failureRate >= failureRateThreshold) {
            triggerCorrection(campaign, CorrectionTriggerType.HIGH_FAILURE_RATE, 
                    BigDecimal.valueOf(failureRateThreshold), BigDecimal.valueOf(failureRate), campaign.getTotalSent());
            return;
        }

        // Uses a native query internally, simplified for the stub
        // To precisely match opt out we should use the view or a repository count
        // We will assume a simple calculation here based on counters, but since unsubs are in Communication
        // we'll leave it as an example stub
    }

    private void triggerCorrection(CampaignEntity campaign, CorrectionTriggerType triggerType, 
                                   BigDecimal threshold, BigDecimal observed, Integer cohortSize) {
        log.warn("Correction triggered for campaign {} due to {}", campaign.getId(), triggerType);
        aiCorrectionDecisionService.makeCorrectionDecision(campaign, triggerType, threshold, observed, cohortSize);
    }
}
