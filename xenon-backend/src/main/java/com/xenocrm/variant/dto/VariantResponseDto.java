package com.xenocrm.variant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * VariantResponseDto — Standard DTO representing a campaign variant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponseDto {
    private UUID id;
    private UUID campaignId;
    private String name;
    private String contentTemplate;
    private String language;
    private Double alpha;
    private Double beta;
    private boolean isControl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
