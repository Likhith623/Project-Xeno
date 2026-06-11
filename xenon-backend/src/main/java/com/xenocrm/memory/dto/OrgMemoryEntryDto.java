package com.xenocrm.memory.dto;

import com.xenocrm.memory.enums.LearningType;
import com.xenocrm.variant.enums.MessageChannel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class OrgMemoryEntryDto {
    private UUID id;
    private String segmentTag;
    private MessageChannel channel;
    private LearningType learningType;
    private String learningSummary;
    private BigDecimal confidence;
    private Integer evidenceCount;
    private BigDecimal avgLift;
    private Map<String, Object> winningCopySignals;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
