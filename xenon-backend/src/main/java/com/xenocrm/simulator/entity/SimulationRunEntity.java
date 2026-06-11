package com.xenocrm.simulator.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.simulator.enums.SimulationRunStatus;
import com.xenocrm.variant.entity.MessageVariantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * SimulationRunEntity — JPA entity mapping to the `simulation_runs` table.
 * Layer: Domain Entity
 */
@Entity
@Table(name = "simulation_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SimulationRunEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaign;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SimulationRunStatus status; // DEFAULT 'pending'

    @Column(name = "synthetic_audience_size")
    private int syntheticAudienceSize; // DEFAULT 500

    @Column(name = "predicted_open_rate", precision = 5, scale = 4)
    private BigDecimal predictedOpenRate;

    @Column(name = "predicted_ctr", precision = 5, scale = 4)
    private BigDecimal predictedCtr;

    @Column(name = "predicted_conversion_rate", precision = 5, scale = 4)
    private BigDecimal predictedConversionRate;

    @Column(name = "predicted_revenue", precision = 12, scale = 2)
    private BigDecimal predictedRevenue;

    @Column(name = "confidence_interval_low", precision = 5, scale = 4)
    private BigDecimal confidenceIntervalLow;

    @Column(name = "confidence_interval_high", precision = 5, scale = 4)
    private BigDecimal confidenceIntervalHigh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winning_variant_id")
    private MessageVariantEntity winningVariant;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "persona_distribution", columnDefinition = "jsonb")
    private Map<String, Object> personaDistribution; // e.g. {"high_value":0.3,"new":0.2}

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
