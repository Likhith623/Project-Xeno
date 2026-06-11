package com.xenocrm.channelstub;

import com.xenocrm.channelservice.dto.ChannelCallbackPayloadDto;
import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import com.xenocrm.channelservice.enums.ChannelCallbackEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * ChannelStubSimulationService — Simulates the asynchronous lifecycle of a message and posts callbacks back to CRM.
 * Layer: Service
 */
@Service
@Profile("stub")
@RequiredArgsConstructor
@Slf4j
public class ChannelStubSimulationService {

    private final ChannelStubOutcomeGenerator outcomeGenerator;

    @Value("${channel.crm.callback-url}")
    private String crmCallbackUrl;

    @Async
    public CompletableFuture<Void> simulateLifecycleEvents(String messageId, ChannelSendRequestDto requestDto) {
        try {
            RestClient restClient = RestClient.builder().baseUrl(crmCallbackUrl).build();

            // Simulate slight network delay
            Thread.sleep(500);

            if (!outcomeGenerator.shouldDeliver(requestDto)) {
                sendCallback(restClient, messageId, ChannelCallbackEventType.FAILED, Map.of("error_message", "Delivery failed", "error_code", "ERR_500"));
                return CompletableFuture.completedFuture(null);
            }

            sendCallback(restClient, messageId, ChannelCallbackEventType.DELIVERED, Map.of());
            
            // Wait before open
            Thread.sleep(1000);

            if (outcomeGenerator.shouldOpen(requestDto)) {
                sendCallback(restClient, messageId, ChannelCallbackEventType.OPENED, Map.of());
                
                if (outcomeGenerator.shouldUnsubscribe(requestDto)) {
                    sendCallback(restClient, messageId, ChannelCallbackEventType.UNSUBSCRIBED, Map.of());
                    return CompletableFuture.completedFuture(null);
                }

                if (outcomeGenerator.shouldClick(requestDto)) {
                    sendCallback(restClient, messageId, ChannelCallbackEventType.CLICKED, Map.of());

                    // Wait before conversion
                    Thread.sleep(2000);

                    if (outcomeGenerator.shouldConvert(requestDto)) {
                        sendCallback(restClient, messageId, ChannelCallbackEventType.CONVERTED, Map.of());
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

    private void sendCallback(RestClient restClient, String messageId, ChannelCallbackEventType eventType, Map<String, Object> payload) {
        try {
            ChannelCallbackPayloadDto dto = new ChannelCallbackPayloadDto();
            dto.setChannelMessageId(messageId);
            dto.setEventType(eventType);
            dto.setPayload(payload);

            restClient.post()
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
            
            log.info("Stub sent callback {} for message {}", eventType, messageId);
        } catch (Exception e) {
            log.error("Stub failed to send callback: {}", e.getMessage());
        }
    }
}
