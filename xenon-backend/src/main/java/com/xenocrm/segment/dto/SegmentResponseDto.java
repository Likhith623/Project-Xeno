package com.xenocrm.segment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * SegmentResponseDto — Standard DTO representing a segment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String queryDsl;
    private String compiledSql;
    private String[] tags;
    private boolean isDynamic;
    private Integer lastEvaluatedCount;
    private OffsetDateTime lastEvaluatedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
