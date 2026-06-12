package com.xenocrm.campaign.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.communication.service.EmailDispatchService;
import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import com.xenocrm.segment.repository.AudienceSegmentRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * CampaignExecutionService — Handles async execution of campaigns and dispatches real emails.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignExecutionService {

    private final CampaignRepository campaignRepository;
    private final AudienceSegmentRepository segmentRepository;
    private final MessageVariantRepository variantRepository;
    private final EmailDispatchService emailDispatchService;
    private final JdbcTemplate jdbcTemplate;

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> executeCampaignAsync(UUID id) {
        log.info("Starting async execution for campaign: {}", id);

        return CompletableFuture.supplyAsync(() -> {
            CampaignEntity campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));

            campaign.setStatus(CampaignStatus.RUNNING);
            campaign.setStartedAt(OffsetDateTime.now());
            campaignRepository.save(campaign);

            try {
                // 1. Fetch the segment
                AudienceSegmentEntity segment = segmentRepository.findById(campaign.getTargetSegment().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", campaign.getTargetSegment().getId()));

                // 2. Fetch the variants
                List<MessageVariantEntity> variants = variantRepository.findAllByCampaignId(id);
                if (variants.isEmpty()) {
                    log.warn("No variants found for campaign {}. Skipping dispatch.", id);
                    campaign.setStatus(CampaignStatus.COMPLETED);
                    campaignRepository.save(campaign);
                    return null;
                }
                
                // For simplicity, we just use the first active variant
                MessageVariantEntity variant = variants.get(0);
                log.info("Using variant {} ({}) for campaign {}", variant.getId(), variant.getSubjectLine(), id);

                // 3. Evaluate Segment to get emails
                String filterSql = segment.getFilterSql();
                List<String> emails;
                if (filterSql == null || filterSql.isBlank()) {
                    log.warn("Segment {} has no filter SQL. Fetching all valid emails.", segment.getId());
                    emails = jdbcTemplate.queryForList("SELECT email FROM customers WHERE email IS NOT NULL", String.class);
                } else {
                    log.info("Evaluating segment SQL: {}", filterSql);
                    // Extract customer IDs and join with customers table to get emails
                    // The filterSql typically returns 'id' (e.g. SELECT id FROM customers...)
                    String emailQuery = "SELECT c.email FROM customers c WHERE c.id IN (" + filterSql + ") AND c.email IS NOT NULL";
                    emails = jdbcTemplate.queryForList(emailQuery, String.class);
                }

                log.info("Found {} target customers for campaign {}", emails.size(), id);

                // 4. Dispatch Emails
                for (String email : emails) {
                    try {
                        emailDispatchService.sendEmail(email, variant.getSubjectLine(), variant.getBodyHtml());
                        variantRepository.incrementMabImpressions(variant.getId());
                    } catch (Exception e) {
                        log.error("Error sending email to {}: {}", email, e.getMessage());
                    }
                }

                campaign.setStatus(CampaignStatus.COMPLETED);
                campaign.setCompletedAt(OffsetDateTime.now());
                campaign.setTotalSent(emails.size());
                campaignRepository.save(campaign);
                
                log.info("Successfully executed campaign: {}", id);

            } catch (Exception e) {
                log.error("Failed to execute campaign {}: {}", id, e.getMessage(), e);
                campaign.setStatus(CampaignStatus.FAILED);
                campaign.setCompletedAt(OffsetDateTime.now());
                campaignRepository.save(campaign);
            }
            return null;
        });
    }
}
