package com.xenocrm.agent.entity;

import com.xenocrm.agent.enums.AgentSessionStatus;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * AgentSessionEntity — JPA entity mapping to the `agent_sessions` table.
 */
@Entity
@Table(name = "agent_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AgentSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id")
    private UUID userId; // Optional, to tie back to the invoking user

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AgentSessionStatus status; // DEFAULT 'active'

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "context_snapshot", columnDefinition = "jsonb")
    private Map<String, Object> contextSnapshot; // The state of CRM when invoked

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "message_history", columnDefinition = "jsonb")
    private List<Map<String, Object>> messageHistory; // Gemini message array (chat history)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
