package com.xenocrm.correction.dto;

import com.xenocrm.correction.enums.CorrectionActionType;
import com.xenocrm.variant.enums.MessageChannel;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * CorrectionDecisionDto — DTO representing the AI's decision on how to correct a campaign.
 */
@Data
@Builder
public class CorrectionDecisionDto {
    private CorrectionActionType actionTaken;
    private MessageChannel oldChannel;
    private MessageChannel newChannel;
    private UUID oldVariantId;
    private UUID newVariantId;
    private String aiReasoning;
}
