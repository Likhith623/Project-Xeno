package com.xenocrm.correction.dto;

import com.xenocrm.correction.enums.CorrectionActionType;
import com.xenocrm.correction.enums.CorrectionOutcome;
import com.xenocrm.correction.enums.CorrectionTriggerType;
import com.xenocrm.channelservice.enums.MessageChannel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CorrectionEventResponseDto — DTO for responding with correction event details.
 */
@Data
public class CorrectionEventResponseDto {
    private UUID id;
    private UUID campaignId;
    private CorrectionTriggerType triggerType;
    private BigDecimal triggerThreshold;
    private BigDecimal observedValue;
    private Integer cohortSize;
    private CorrectionActionType actionTaken;
    private MessageChannel oldChannel;
    private MessageChannel newChannel;
    private UUID oldVariantId;
    private UUID newVariantId;
    private String aiReasoning;
    private CorrectionOutcome correctionOutcome;
    private BigDecimal outcomeDelta;
    private OffsetDateTime createdAt;
    private OffsetDateTime evaluatedAt;
}
