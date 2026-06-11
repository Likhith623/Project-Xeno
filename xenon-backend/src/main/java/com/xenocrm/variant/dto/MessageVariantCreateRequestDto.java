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

/** MessageVariantCreateRequestDto -- DTO for creating a message variant. Layer: DTO */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageVariantCreateRequestDto {
    @NotNull private UUID campaignId;
    @NotBlank private String name;
    @NotNull private MessageChannel channel;
    private String subjectLine;
    private String previewText;
    private String bodyText;
    private String bodyHtml;
    private String ctaText;
    private String ctaUrl;
    private String mediaUrl;
    private String templateId;
    private Map<String, Object> templateParams;
    private boolean generatedByAi;
    private String generationPrompt;
}
