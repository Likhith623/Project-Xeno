package com.xenocrm.segment.dto;

import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.enums.SegmentStatus;
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
public class SegmentResponseDto {

    private UUID id;
    private String name;
    private String description;
    private SegmentType type;
    private SegmentStatus status;
    private String filterSql;
    private Map<String, Object> filterJson;
    private boolean isPinned;
    private boolean createdByAgent;
    private String agentGoal;
    private int customerCount;
    private OffsetDateTime lastEvaluatedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
