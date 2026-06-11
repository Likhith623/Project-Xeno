package com.xenocrm.channelservice.dto;

import com.xenocrm.channelservice.enums.ChannelCallbackEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * ChannelCallbackPayloadDto — DTO for receiving a channel callback payload.
 */
@Data
public class ChannelCallbackPayloadDto {

    @NotBlank(message = "Channel message ID is required")
    private String channelMessageId;

    @NotNull(message = "Event type is required")
    private ChannelCallbackEventType eventType;

    @NotNull(message = "Payload is required")
    private Map<String, Object> payload;
}
