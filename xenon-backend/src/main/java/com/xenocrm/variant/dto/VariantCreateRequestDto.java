package com.xenocrm.variant.dto;

import com.xenocrm.channelservice.enums.MessageChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantCreateRequestDto {

    @NotNull
    private UUID campaignId;

    @NotNull
    private MessageChannel channel;

    @NotBlank
    private String copyText;

    private String templateId;

    private Map<String, Object> templateParams;
}
