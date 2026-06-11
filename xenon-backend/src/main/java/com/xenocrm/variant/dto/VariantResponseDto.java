package com.xenocrm.variant.dto;

import com.xenocrm.variant.enums.MessageChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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
    private String name;
    private MessageChannel channel;
    
    private String subjectLine;
    private String previewText;
    private String bodyText;
    private String bodyHtml;
    private String ctaText;
    private String ctaUrl;
    private String mediaUrl;
    private String templateId;
    private Map<String, Object> templateParams;

    private BigDecimal mabAlpha;
    private BigDecimal mabBeta;
    private int mabImpressions;
    private int mabConversions;
    private boolean mabIsActive;

    private boolean generatedByAi;
    private String generationPrompt;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
