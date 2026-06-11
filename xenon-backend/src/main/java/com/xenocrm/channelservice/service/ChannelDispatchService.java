package com.xenocrm.channelservice.service;

import com.xenocrm.channelservice.dto.ChannelSendRequestDto;
import com.xenocrm.channelservice.dto.ChannelSendResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * ChannelDispatchService — Service for dispatching messages to the external channel provider (or stub).
 * Layer: Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelDispatchService {

    @Value("${channel.stub.base-url}")
    private String channelStubBaseUrl;

    /**
     * Dispatches a message to the external channel service.
     *
     * @param requestDto The request containing recipient, channel, and content.
     * @return ChannelSendResponseDto containing the channelMessageId if successful.
     */
    public ChannelSendResponseDto dispatchMessage(ChannelSendRequestDto requestDto) {
        try {
            RestClient restClient = RestClient.builder().baseUrl(channelStubBaseUrl).build();
            return restClient.post()
                    .uri("/api/v1/stub/send")
                    .body(requestDto)
                    .retrieve()
                    .body(ChannelSendResponseDto.class);
        } catch (Exception e) {
            log.error("Failed to dispatch message to channel service: {}", e.getMessage());
            return ChannelSendResponseDto.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}
