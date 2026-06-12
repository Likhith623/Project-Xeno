package com.xenocrm.channelservice.service;

import com.xenocrm.channelservice.dto.ChannelCallbackPayloadDto;
import com.xenocrm.channelservice.entity.ChannelCallbackEntity;
import com.xenocrm.channelservice.enums.CallbackProcessingStatus;
import com.xenocrm.channelservice.repository.ChannelCallbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CallbackProcessingService — Receives channel callbacks and stores them in the channel_callbacks table.
 * Layer: Service
 * Note: A background @Scheduled worker (CallbackRetryWorker) processes these events to handle race conditions gracefully.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackProcessingService {

    private final ChannelCallbackRepository channelCallbackRepository;
    private final com.xenocrm.communication.repository.CommunicationRepository communicationRepository;

    /**
     * Ingests a raw webhook event from the channel provider.
     * The callback is saved as PENDING and picked up by CallbackRetryWorker.
     *
     * @param payloadDto The callback payload
     */
    @Transactional
    public void processCallback(ChannelCallbackPayloadDto payloadDto) {
        log.info("Received callback for message ID: {} with event: {}", 
                payloadDto.getChannelMessageId(), payloadDto.getEventType());

        com.xenocrm.communication.entity.CommunicationEntity comm = null;
        if (payloadDto.getCommunicationId() != null) {
            comm = communicationRepository.findById(payloadDto.getCommunicationId()).orElse(null);
        }

        ChannelCallbackEntity callbackEntity = ChannelCallbackEntity.builder()
                .channelMessageId(payloadDto.getChannelMessageId())
                .communication(comm) // set the communication if we have it!
                .eventType(payloadDto.getEventType())
                .payload(payloadDto.getPayload())
                .processingStatus(CallbackProcessingStatus.PENDING)
                .build();

        // The save persists it. CallbackRetryWorker will process it asynchronously.
        channelCallbackRepository.save(callbackEntity);
    }
}
