package com.xenocrm.channelservice.service;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.campaign.repository.CampaignRepository;
import com.xenocrm.channelservice.entity.ChannelCallbackEntity;
import com.xenocrm.channelservice.enums.CallbackProcessingStatus;
import com.xenocrm.channelservice.enums.ChannelCallbackEventType;
import com.xenocrm.channelservice.repository.ChannelCallbackRepository;
import com.xenocrm.communication.entity.CommunicationEntity;
import com.xenocrm.communication.repository.CommunicationRepository;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.repository.MessageVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * CallbackRetryWorker — Asynchronously polls PENDING callbacks and processes them.
 * This completely replaces the fn_apply_callback Postgres trigger to gracefully handle
 * race conditions, duplicate events (idempotency), and retries.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackRetryWorker {

    private final ChannelCallbackRepository callbackRepository;
    private final CommunicationRepository communicationRepository;
    private final CampaignRepository campaignRepository;
    private final MessageVariantRepository variantRepository;

    @Scheduled(fixedDelay = 5000)
    public void processPendingCallbacks() {
        List<ChannelCallbackEntity> pending = callbackRepository.findAllByProcessingStatus(CallbackProcessingStatus.PENDING);
        for (ChannelCallbackEntity callback : pending) {
            try {
                processSingleCallback(callback);
            } catch (Exception e) {
                log.error("Failed to process callback ID {}: {}", callback.getId(), e.getMessage());
                handleFailure(callback, e.getMessage());
            }
        }
    }

    @Transactional
    public void processSingleCallback(ChannelCallbackEntity callback) {
        log.debug("Processing callback: {}", callback.getId());

        // 1. Resolve Communication ID
        CommunicationEntity comm = null;
        if (callback.getCommunication() != null) {
            comm = callback.getCommunication();
        } else if (callback.getChannelMessageId() != null) {
            comm = communicationRepository.findByChannelMessageId(callback.getChannelMessageId()).orElse(null);
        }

        // RACE CONDITION HANDLING:
        if (comm == null) {
            throw new RuntimeException("Could not resolve communication_id yet (potential race condition)");
        }

        // 2. Idempotency Guard
        // We ensure we don't process the same event twice for the same communication
        // Actually, we should check if this specific event already happened for this communication
        // But for simplicity, we just check if this specific callback ID was already processed,
        // Wait, if a duplicate callback comes from the network, it creates a NEW ChannelCallbackEntity!
        // So we must check if the communication ALREADY HAS this event.
        if (isIdempotentDuplicate(comm, callback.getEventType())) {
            log.info("Duplicate callback detected (Idempotency Guard). CommId: {}, Event: {}", comm.getId(), callback.getEventType());
            callback.setProcessingStatus(CallbackProcessingStatus.PROCESSED);
            callback.setProcessedAt(OffsetDateTime.now());
            callback.setProcessingError("Duplicate Ignored");
            callbackRepository.save(callback);
            return;
        }

        // 3. Apply state machine to Communication
        applyState(comm, callback);
        communicationRepository.save(comm);

        // 4. Update Campaign Counters
        if (comm.getCampaign() != null) {
            CampaignEntity campaign = comm.getCampaign();
            updateCampaignCounters(campaign, callback.getEventType());
            campaignRepository.save(campaign);
        }

        // 5. Update Thompson Sampling (MAB) Variants
        if (comm.getVariant() != null) {
            MessageVariantEntity variant = comm.getVariant();
            updateVariantMab(variant, callback.getEventType());
            variantRepository.save(variant);
        }

        // 6. Mark Success
        callback.setCommunication(comm);
        callback.setProcessingStatus(CallbackProcessingStatus.PROCESSED);
        callback.setProcessedAt(OffsetDateTime.now());
        callbackRepository.save(callback);
    }

    private boolean isIdempotentDuplicate(CommunicationEntity comm, ChannelCallbackEventType event) {
        return switch (event) {
            case DELIVERED -> comm.getDeliveredAt() != null;
            case OPENED -> comm.getOpenedAt() != null;
            case CLICKED -> comm.getClickedAt() != null;
            case CONVERTED -> comm.getConvertedAt() != null;
            case FAILED -> comm.getFailedAt() != null;
            case UNSUBSCRIBED -> comm.getUnsubscribedAt() != null;
            default -> false;
        };
    }

    private void applyState(CommunicationEntity comm, ChannelCallbackEntity callback) {
        comm.setStatus(com.xenocrm.communication.enums.CommunicationStatus.valueOf(callback.getEventType().name()));
        ChannelCallbackEventType event = callback.getEventType();
        
        if (event == ChannelCallbackEventType.DELIVERED) comm.setDeliveredAt(OffsetDateTime.now());
        if (event == ChannelCallbackEventType.OPENED) comm.setOpenedAt(OffsetDateTime.now());
        if (event == ChannelCallbackEventType.CLICKED) comm.setClickedAt(OffsetDateTime.now());
        if (event == ChannelCallbackEventType.CONVERTED) comm.setConvertedAt(OffsetDateTime.now());
        if (event == ChannelCallbackEventType.FAILED) comm.setFailedAt(OffsetDateTime.now());
        if (event == ChannelCallbackEventType.UNSUBSCRIBED) comm.setUnsubscribedAt(OffsetDateTime.now());

        if (callback.getPayload().containsKey("error_message")) {
            comm.setFailureReason(String.valueOf(callback.getPayload().get("error_message")));
        }
        if (callback.getPayload().containsKey("error_code")) {
            comm.setFailureCode(String.valueOf(callback.getPayload().get("error_code")));
        }
    }

    private void updateCampaignCounters(CampaignEntity campaign, ChannelCallbackEventType event) {
        if (event == ChannelCallbackEventType.DELIVERED) campaign.setTotalDelivered(campaign.getTotalDelivered() + 1);
        if (event == ChannelCallbackEventType.OPENED) campaign.setTotalOpened(campaign.getTotalOpened() + 1);
        if (event == ChannelCallbackEventType.CLICKED) campaign.setTotalClicked(campaign.getTotalClicked() + 1);
        if (event == ChannelCallbackEventType.CONVERTED) campaign.setTotalConverted(campaign.getTotalConverted() + 1);
        if (event == ChannelCallbackEventType.FAILED) campaign.setTotalFailed(campaign.getTotalFailed() + 1);
    }

    private void updateVariantMab(MessageVariantEntity variant, ChannelCallbackEventType event) {
        // Increment MAB impressions on delivery
        if (event == ChannelCallbackEventType.DELIVERED) {
            variant.setMabImpressions(variant.getMabImpressions() + 1);
            variant.setMabBeta(variant.getMabBeta().add(java.math.BigDecimal.ONE));
        }
        // Increment MAB conversions on click or convert
        if (event == ChannelCallbackEventType.CLICKED || event == ChannelCallbackEventType.CONVERTED) {
            // Note: to perfectly match SQL we only increment once. If clicking and converting both happen, it increments twice.
            // But the SQL did: IF event IN ('converted','clicked') THEN mab_conversions += 1, alpha += 1.
            variant.setMabConversions(variant.getMabConversions() + 1);
            variant.setMabAlpha(variant.getMabAlpha().add(java.math.BigDecimal.ONE));
        }
    }

    @Transactional
    public void handleFailure(ChannelCallbackEntity callback, String error) {
        int retries = callback.getRetryCount() != null ? callback.getRetryCount() : 0;
        retries++;
        callback.setRetryCount(retries);
        callback.setProcessingError(error);

        if (retries > 5) {
            callback.setProcessingStatus(CallbackProcessingStatus.ERROR);
            log.error("Callback {} permanently failed after {} retries.", callback.getId(), retries);
        }
        callbackRepository.save(callback);
    }
}
