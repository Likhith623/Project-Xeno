package com.xenocrm.agent.entity;

import com.xenocrm.agent.enums.AgentDecisionType;
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
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "agent_decisions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AgentDecisionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private AgentSessionEntity session;

    @Column(name = "step_number", nullable = false)
    private int stepNumber;

    
    @Column(name = "decision_type", nullable = false)
    private AgentDecisionType decisionType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "input_context", columnDefinition = "jsonb")
    private Map<String, Object> inputContext;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "output_action", columnDefinition = "jsonb")
    private Map<String, Object> outputAction;

    @Column(name = "reasoning", columnDefinition = "TEXT")
    private String reasoning;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
