package com.xenocrm.segment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class SegmentMemberResponseDto {
    private UUID segmentId;
    private UUID customerId;
    private OffsetDateTime addedAt;
}
