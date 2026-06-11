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
 * Note: A Postgres trigger (fn_apply_callback) runs on INSERT to process the callback.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackProcessingService {

    private final ChannelCallbackRepository channelCallbackRepository;

    /**
     * Ingests a raw webhook event from the channel provider.
     * The DB trigger fn_apply_callback will handle updating the CommunicationEntity
     * and Thompson Sampling parameters upon insertion.
     *
     * @param payloadDto The callback payload
     */
    @Transactional
    public void processCallback(ChannelCallbackPayloadDto payloadDto) {
        log.info("Received callback for message ID: {} with event: {}", 
                payloadDto.getChannelMessageId(), payloadDto.getEventType());

        ChannelCallbackEntity callbackEntity = ChannelCallbackEntity.builder()
                .channelMessageId(payloadDto.getChannelMessageId())
                .eventType(payloadDto.getEventType())
                .payload(payloadDto.getPayload())
                .processingStatus(CallbackProcessingStatus.PENDING)
                .build();

        // The save triggers fn_apply_callback which updates the status to PROCESSED (or ERROR)
        channelCallbackRepository.save(callbackEntity);
    }
}
