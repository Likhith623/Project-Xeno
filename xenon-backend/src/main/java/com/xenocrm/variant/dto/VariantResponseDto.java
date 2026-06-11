package com.xenocrm.variant.dto;

import com.xenocrm.channelservice.enums.MessageChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponseDto {

    private UUID id;
    private UUID campaignId;
    private MessageChannel channel;
    private String copyText;
    private String templateId;
    private Map<String, Object> templateParams;
    private boolean isWinner;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
