package com.xenocrm.correction.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.correction.enums.CorrectionActionType;
import com.xenocrm.correction.enums.CorrectionOutcome;
import com.xenocrm.correction.enums.CorrectionTriggerType;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.channelservice.enums.MessageChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CorrectionEventEntity — JPA entity mapping to the `correction_events` table.
 * Layer: Domain Entity
 * Purpose: Records every correction decision: why it fired, what it changed, outcome.
 * Relationships: ManyToOne with CampaignEntity, ManyToOne with MessageVariantEntity
 */
@Entity
@Table(name = "correction_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CorrectionEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaign;

    
    @Column(name = "trigger_type", nullable = false)
    private CorrectionTriggerType triggerType;

    @Column(name = "trigger_threshold", precision = 8, scale = 4)
    private BigDecimal triggerThreshold;

    @Column(name = "observed_value", precision = 8, scale = 4)
    private BigDecimal observedValue;

    @Column(name = "cohort_size")
    private Integer cohortSize;

    
    @Column(name = "action_taken", nullable = false)
    private CorrectionActionType actionTaken;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_channel", columnDefinition = "message_channel")
    private MessageChannel oldChannel; // nullable

    @Enumerated(EnumType.STRING)
    @Column(name = "new_channel", columnDefinition = "message_channel")
    private MessageChannel newChannel; // nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_variant_id")
    private MessageVariantEntity oldVariant; // nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_variant_id")
    private MessageVariantEntity newVariant; // nullable

    @Column(name = "ai_reasoning", columnDefinition = "TEXT")
    private String aiReasoning; // LLM explanation stored verbatim for audit

    
    @Column(name = "correction_outcome")
    private CorrectionOutcome correctionOutcome; // nullable — filled in after correction runs

    @Column(name = "outcome_delta", precision = 8, scale = 4)
    private BigDecimal outcomeDelta; // nullable — e.g. 0.04 = CTR improved by 4pp

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "evaluated_at")
    private OffsetDateTime evaluatedAt; // nullable — when outcome was assessed
}
