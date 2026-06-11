package com.xenocrm.correction.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.correction.dto.CorrectionDecisionDto;
import com.xenocrm.correction.entity.CorrectionEventEntity;
import com.xenocrm.correction.enums.CorrectionActionType;
import com.xenocrm.correction.enums.CorrectionTriggerType;
import com.xenocrm.correction.repository.CorrectionEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * CorrectionExecutionService — Executes the chosen correction and logs the event.
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CorrectionExecutionService {

    private final CorrectionEventRepository correctionEventRepository;
    private final CampaignRepository campaignRepository;

    @Transactional
    public void executeCorrection(CampaignEntity campaign, CorrectionTriggerType triggerType, 
                                  BigDecimal threshold, BigDecimal observed, Integer cohortSize, 
                                  CorrectionDecisionDto decision) {
        
        log.info("Executing correction {} for campaign {}", decision.getActionTaken(), campaign.getId());

        if (decision.getActionTaken() == CorrectionActionType.PAUSE_CAMPAIGN) {
            campaign.setStatus(CampaignStatus.PAUSED);
            campaignRepository.save(campaign);
        }
        
        // Other actions (SWITCH_CHANNEL, REWRITE_COPY) would be implemented here

        CorrectionEventEntity event = CorrectionEventEntity.builder()
                .campaign(campaign)
                .triggerType(triggerType)
                .triggerThreshold(threshold)
                .observedValue(observed)
                .cohortSize(cohortSize)
                .actionTaken(decision.getActionTaken())
                .oldChannel(decision.getOldChannel())
                .newChannel(decision.getNewChannel())
                .aiReasoning(decision.getAiReasoning())
                // In a real scenario we'd fetch the variant entities 
                .build();

        correctionEventRepository.save(event);
    }
}
