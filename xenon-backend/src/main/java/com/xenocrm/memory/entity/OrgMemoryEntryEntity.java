package com.xenocrm.memory.entity;

import com.xenocrm.memory.enums.MemoryLearningType;
import com.xenocrm.memory.enums.TimeOfDay;
import com.xenocrm.channelservice.enums.MessageChannel;
import io.hypersistence.utils.hibernate.type.array.UUIDArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

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

    @Column(name = "segment_tag")
    private String segmentTag;

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.Type(io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType.class)
    @Column(name = "channel", columnDefinition = "message_channel")
    private MessageChannel channel;

    @Column(name = "day_of_week")
    private Integer dayOfWeek;

    
    @Column(name = "time_of_day")
    private TimeOfDay timeOfDay;

    
    @Column(name = "learning_type", nullable = false)
    private MemoryLearningType learningType;

    @Column(name = "learning_summary", columnDefinition = "TEXT", nullable = false)
    private String learningSummary;

    @Column(name = "confidence")
    private BigDecimal confidence;

    @Type(UUIDArrayType.class)
    @Column(name = "source_campaign_ids", columnDefinition = "uuid[]")
    private UUID[] sourceCampaignIds;

    @Column(name = "evidence_count")
    private int evidenceCount;

    @Column(name = "avg_lift")
    private BigDecimal avgLift;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "winning_copy_signals", columnDefinition = "jsonb")
    private Map<String, Object> winningCopySignals;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
