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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AgentSessionStatus status; // DEFAULT 'running'

    @Column(name = "model_used")
    private String modelUsed; // DEFAULT 'gemini-2.5-pro'

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "conversation_log", columnDefinition = "jsonb")
    private List<Map<String, Object>> conversationLog; // JSONB DEFAULT '[]'

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "plan", columnDefinition = "jsonb")
    private Map<String, Object> plan;

    @Column(name = "created_segment_id")
    private UUID createdSegmentId;

    @Column(name = "created_campaign_id")
    private UUID createdCampaignId;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "tokens_used_in")
    private int tokensUsedIn;

    @Column(name = "tokens_used_out")
    private int tokensUsedOut;
}
