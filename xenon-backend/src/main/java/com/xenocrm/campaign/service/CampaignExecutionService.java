package com.xenocrm.campaign.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CampaignExecutionService — Handles async execution of campaigns.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignExecutionService {

    private final CampaignRepository campaignRepository;

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> executeCampaignAsync(UUID id) {
        log.debug("Starting async execution for campaign: {}", id);

        return CompletableFuture.supplyAsync(() -> {
            CampaignEntity campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));

            campaign.setStatus(CampaignStatus.ACTIVE);
            campaign.setStartedAt(OffsetDateTime.now());
            campaignRepository.save(campaign);

            // Simulation of execution logic: evaluate segment, fetch variants, send comms
            log.debug("Campaign {} execution in progress...", id);

            // Once complete, update status
            campaign.setStatus(CampaignStatus.COMPLETED);
            campaign.setCompletedAt(OffsetDateTime.now());
            campaignRepository.save(campaign);
            
            log.debug("Successfully executed campaign: {}", id);
            return null;
        });
    }
}
