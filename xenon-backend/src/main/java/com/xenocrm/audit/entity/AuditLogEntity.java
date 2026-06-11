package com.xenocrm.audit.entity;

import com.xenocrm.audit.enums.AuditActorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLogEntity — JPA entity mapping to the `audit_logs` table.
 * Layer: Domain Entity
 * Purpose: Logs every important action performed by the agent, system, or user.
 */
@Entity
@Table(name = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // BIGSERIAL PRIMARY KEY — NOT UUID

    @Column(name = "trace_id")
    private UUID traceId; // nullable — groups related events in one HTTP request

    @Column(name = "entity_type", length = 100, nullable = false)
    private String entityType; // e.g. "CampaignEntity", "AgentSession"

    @Column(name = "entity_id", nullable = false)
    private Long entityId; // BIGINT — use 0L for UUID-keyed entities

    @Column(name = "action", length = 50, nullable = false)
    private String action; // "CREATE","UPDATE","DELETE","LAUNCH","PAUSE","CORRECT"

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", length = 50, nullable = false)
    private AuditActorType actorType; // USER | AGENT | SYSTEM

    @Column(name = "actor_id", length = 255)
    private String actorId; // nullable — API key hash, agent session ID, or "scheduler"

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "old_value", columnDefinition = "jsonb")
    private Map<String, Object> oldValue; // State before the action — nullable

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_value", columnDefinition = "jsonb")
    private Map<String, Object> newValue; // State after the action — nullable

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // Human-readable summary — use to store UUID for UUID-keyed entities

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // Uses LocalDateTime NOT OffsetDateTime
}
