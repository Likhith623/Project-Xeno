package com.xenocrm.memory.dto;

import com.xenocrm.memory.enums.MemoryLearningType;
import com.xenocrm.memory.enums.TimeOfDay;
import com.xenocrm.channelservice.enums.MessageChannel;
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
public class OrgMemoryEntryDto {

    private UUID id;
    private String segmentTag;
    private MessageChannel channel;
    private MemoryLearningType learningType;
    private String learningSummary;
    private BigDecimal confidence;
    private Integer evidenceCount;
    private BigDecimal avgLift;
    private Map<String, Object> winningCopySignals;
    private Integer dayOfWeek;
    private TimeOfDay timeOfDay;
    private UUID[] sourceCampaignIds;
    private boolean isActive;
    private OffsetDateTime expiresAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
