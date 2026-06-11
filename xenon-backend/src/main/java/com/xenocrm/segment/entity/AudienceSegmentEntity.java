package com.xenocrm.segment.entity;

import com.xenocrm.segment.enums.SegmentStatus;
import com.xenocrm.segment.enums.SegmentType;
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

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audience_segments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AudienceSegmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SegmentType type; // 'static','dynamic','ai_generated' DEFAULT 'dynamic'

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SegmentStatus status; // 'draft','building','ready','archived' DEFAULT 'draft'

    @Column(name = "filter_sql", columnDefinition = "TEXT")
    private String filterSql; // WHERE clause fragment — NEVER interpolated raw into SQL

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "filter_json", columnDefinition = "jsonb")
    private Map<String, Object> filterJson; // Human-readable filter for the UI

    @Column(name = "is_pinned")
    private boolean isPinned; // DEFAULT FALSE — static segment, not re-evaluated

    @Column(name = "created_by_agent")
    private boolean createdByAgent; // DEFAULT FALSE

    @Column(name = "agent_goal")
    private String agentGoal; // NL goal that spawned this segment — nullable

    @Column(name = "customer_count")
    private int customerCount; // DEFAULT 0 — updated by fn_evaluate_segment

    @Column(name = "last_evaluated_at")
    private OffsetDateTime lastEvaluatedAt; // nullable

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
