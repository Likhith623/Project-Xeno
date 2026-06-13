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
import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import com.xenocrm.channelservice.dto.ChannelSendResponseDto;
import com.xenocrm.channelservice.service.ChannelDispatchService;
import com.xenocrm.channelservice.enums.MessageChannel;
import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.communication.repository.CommunicationRepository;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.customer.repository.CustomerRepository;
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
    private final ChannelDispatchService channelDispatchService;
    private final CommunicationRepository communicationRepository;
    private final CustomerRepository customerRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EmailDispatchService emailDispatchService;

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> executeCampaignAsync(UUID id) {
        log.info("Starting async execution for campaign: {}", id);

        try {
            CampaignEntity campaign = campaignRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Campaign", "id", id));

            campaign.setStatus(CampaignStatus.RUNNING);
            campaign.setStartedAt(OffsetDateTime.now());
            campaign = campaignRepository.save(campaign);

            final UUID segmentId = campaign.getTargetSegment().getId();
            AudienceSegmentEntity segment = segmentRepository.findById(segmentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", segmentId));

            // 2. Fetch the variants
            List<MessageVariantEntity> variants = variantRepository.findAllByCampaignId(id);
            if (variants.isEmpty()) {
                log.warn("No variants found for campaign {}. Skipping dispatch.", id);
                campaign.setStatus(CampaignStatus.COMPLETED);
                campaignRepository.save(campaign);
                return CompletableFuture.completedFuture(null);
            }
            
            // For simplicity, we just use the first active variant
            MessageVariantEntity variant = variants.get(0);
            log.info("Using variant {} ({}) for campaign {}", variant.getId(), variant.getSubjectLine(), id);

            // 3. Evaluate Segment to get emails
            String filterSql = segment.getFilterSql();
            List<java.util.Map<String, Object>> targetRows;
            if (filterSql == null || filterSql.isBlank()) {
                log.warn("Segment {} has no filter SQL. Fetching all valid emails.", segment.getId());
                targetRows = jdbcTemplate.queryForList("SELECT id, email FROM customers WHERE email IS NOT NULL AND is_globally_opted_out = false");
            } else {
                log.info("Evaluating segment SQL: {}", filterSql);
                String emailQuery = "SELECT c.id, c.email FROM customers c WHERE c.id IN (" + filterSql + ") AND c.email IS NOT NULL AND c.is_globally_opted_out = false";
                targetRows = jdbcTemplate.queryForList(emailQuery);
            }

            log.info("Found {} target customers for campaign {}", targetRows.size(), id);
            int sentCount = 0;

            // 4. Dispatch Emails using two-service architecture
            for (java.util.Map<String, Object> row : targetRows) {
                try {
                    UUID customerId = (UUID) row.get("id");
                    String email = (String) row.get("email");
                    CustomerEntity customer = customerRepository.findById(customerId).orElse(null);
                    if (customer == null) continue;

                    CommunicationEntity comm = CommunicationEntity.builder()
                            .campaign(campaign)
                            .variant(variant)
                            .customer(customer)
                            .channel(MessageChannel.email)
                            .status(CommunicationStatus.PENDING)
                            .recipientAddress(email)
                            .personalisedSubject(variant.getSubjectLine())
                            .personalisedBody(variant.getBodyHtml())
                            .build();
                    comm = communicationRepository.save(comm);

                    ChannelSendRequestDto req = ChannelSendRequestDto.builder()
                            .recipientAddress(email)
                            .communicationId(comm.getId())
                            .channel(MessageChannel.email)
                            .subject(variant.getSubjectLine())
                            .body(variant.getBodyHtml())
                            .build();

                    ChannelSendResponseDto res = channelDispatchService.dispatchMessage(req);
                    log.info("DEBUG: Dispatch success for {}: {}", email, res.isSuccess());
                    if (res.isSuccess()) {
                        log.info("DEBUG: Inside success block for {}", email);
                        comm.setChannelMessageId(res.getChannelMessageId());
                        communicationRepository.save(comm);
                        variantRepository.incrementMabImpressions(variant.getId());
                        sentCount++;
                        
                        // Send actual email via SMTP for testing
                        log.info("DEBUG: Checking channel. req.getChannel() = {}", req.getChannel());
                        if (MessageChannel.email.equals(req.getChannel())) {
                            log.info("DEBUG: Calling emailDispatchService for {}", email);
                            try {
                                emailDispatchService.sendEmail(email, variant.getSubjectLine(), variant.getBodyHtml());
                            } catch (Exception mailEx) {
                                log.error("Failed to send real email to {}: {}", email, mailEx.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error sending message: {}", e.getMessage());
                }
            }

            campaign.setStatus(CampaignStatus.COMPLETED);
            campaign.setCompletedAt(OffsetDateTime.now());
            campaignRepository.save(campaign);
            
            for (int k = 0; k < sentCount; k++) {
                campaignRepository.incrementTotalSent(id);
            }
            
            log.info("Successfully executed campaign: {}", id);

        } catch (Exception e) {
            log.error("Failed to execute campaign {}: {}", id, e.getMessage(), e);
            try {
                CampaignEntity errorCamp = campaignRepository.findById(id).orElse(null);
                if(errorCamp != null) {
                    errorCamp.setStatus(CampaignStatus.FAILED);
                    errorCamp.setCompletedAt(OffsetDateTime.now());
                    campaignRepository.save(errorCamp);
                }
            } catch (Exception innerE) {
                log.error("Could not save FAILED state for campaign {}: {}", id, innerE.getMessage());
            }
        }
        return CompletableFuture.completedFuture(null);
    }
}
