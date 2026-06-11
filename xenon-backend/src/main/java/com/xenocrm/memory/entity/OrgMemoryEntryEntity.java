package com.xenocrm.memory.entity;

import com.xenocrm.memory.enums.LearningType;
import com.xenocrm.variant.enums.MessageChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * OrgMemoryEntryEntity — JPA entity mapping to the `org_memory_entries` table.
 * Layer: Domain Entity
 * Purpose: A persistent knowledge base storing generalized insights extracted from past campaigns.
 */
@Entity
@Table(name = "org_memory_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class OrgMemoryEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "segment_tag", length = 100)
    private String segmentTag; // nullable — generalized tag, e.g. "coffee_buyers"

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", columnDefinition = "message_channel")
    private MessageChannel channel; // nullable

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_type", nullable = false)
    private LearningType learningType; // 'copy_style', 'send_time', 'offer_type'

    @Column(name = "learning_summary", columnDefinition = "TEXT", nullable = false)
    private String learningSummary;

    @Column(name = "confidence", precision = 4, scale = 3)
    private BigDecimal confidence; // 0.000 to 1.000

    @Column(name = "evidence_count")
    private Integer evidenceCount; // DEFAULT 1

    @Column(name = "avg_lift", precision = 5, scale = 4)
    private BigDecimal avgLift; // e.g., 0.22 means 22% improvement

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "winning_copy_signals", columnDefinition = "jsonb")
    private Map<String, Object> winningCopySignals;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
