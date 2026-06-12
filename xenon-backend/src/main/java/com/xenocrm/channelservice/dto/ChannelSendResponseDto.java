package com.xenocrm.channelservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * ChannelSendResponseDto — DTO for responding to a channel send request.
 */
@Data
@Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ChannelSendResponseDto {
    private String channelMessageId;
    private boolean success;
    private String errorMessage;
}
