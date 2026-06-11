package com.xenocrm.channelservice.dto;

import com.xenocrm.variant.enums.MessageChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * ChannelSendRequestDto — DTO for sending a message to a channel.
 */
@Data
@Builder
public class ChannelSendRequestDto {

    @NotBlank(message = "Recipient address is required")
    private String recipientAddress;

    @NotNull(message = "Channel is required")
    private MessageChannel channel;

    private String subject;
    
    @NotBlank(message = "Body is required")
    private String body;
}
