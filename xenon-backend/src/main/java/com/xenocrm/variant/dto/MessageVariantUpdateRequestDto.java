package com.xenocrm.variant.dto;

import lombok.Data;

@Data
public class MessageVariantUpdateRequestDto {
    private String name;
    private String channel;
    private String subjectLine;
    private String previewText;
    private String bodyText;
    private String bodyHtml;
    private String ctaText;
    private String ctaUrl;
    private String mediaUrl;
    private String templateId;
    private java.util.Map<String, Object> templateParams;
    private Boolean mabIsActive;
}
