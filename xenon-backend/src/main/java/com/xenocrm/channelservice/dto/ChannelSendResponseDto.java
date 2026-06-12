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
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelSendResponseDto {
    @com.fasterxml.jackson.annotation.JsonAlias("messageId")
    private String channelMessageId;
    private Boolean success;
    private String errorMessage;
    private String status;

    public boolean isSuccess() {
        if (success != null) return success;
        if ("accepted".equalsIgnoreCase(status)) return true;
        return false;
    }
}
