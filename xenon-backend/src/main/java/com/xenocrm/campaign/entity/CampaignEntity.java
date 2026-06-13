package com.xenocrm.campaign.entity;

import com.xenocrm.campaign.enums.CampaignStatus;
import com.xenocrm.segment.entity.AudienceSegmentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CampaignEntity -- JPA entity mapping to the `campaigns` table.
 * Layer: Domain Entity
 * Purpose: Orchestrates a marketing campaign targeting an audience segment.
 * Relationships: ManyToOne targetSegment; ManyToOne parentCampaign (self-ref).
 */
@Entity
@Table(name = "campaigns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CampaignEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;                        // NOT NULL

    @Column(name = "description")
    private String description;                 // nullable

    
    @Column(name = "status")
    private CampaignStatus status;              // draft/simulating/scheduled/running/paused/completed/cancelled/failed

    @Column(name = "goal", columnDefinition = "TEXT")
    private String goal;                        // Marketer NL goal -- nullable

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private AudienceSegmentEntity targetSegment; // FK to audience_segments.id -- nullable

    @Column(name = "scheduled_at")
    private OffsetDateTime scheduledAt;         // nullable

    @Column(name = "started_at")
    private OffsetDateTime startedAt;           // nullable

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;         // nullable

    @Column(name = "timezone")
    private String timezone;                    // DEFAULT Asia/Kolkata

    @Column(name = "max_send_count")
    private Integer maxSendCount;               // nullable -- hard cap on messages dispatched

    @Column(name = "opt_out_rate_threshold", precision = 5, scale = 4)
    private BigDecimal optOutRateThreshold;     // DEFAULT 0.02 -- auto-pause if breached

    @Column(name = "created_by_agent")
    private boolean createdByAgent;             // DEFAULT FALSE

    @Column(name = "agent_session_id")
    private String agentSessionId;              // nullable -- ties back to the agent run

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_campaign_id")
    private CampaignEntity parentCampaign;      // self-referencing FK -- nullable (self-correction retries)

    // Denormalised performance counters -- ALL incremented via @Modifying @Query UPDATE
    @Column(name = "total_sent")
    private int totalSent;                      // DEFAULT 0

    @Column(name = "total_delivered")
    private int totalDelivered;                 // DEFAULT 0

    @Column(name = "total_failed")
    private int totalFailed;                    // DEFAULT 0

    @Column(name = "total_opened")
    private int totalOpened;                    // DEFAULT 0

    @Column(name = "total_read")
    private int totalRead;                      // DEFAULT 0

    @Column(name = "total_clicked")
    private int totalClicked;                   // DEFAULT 0

    @Column(name = "total_converted")
    private int totalConverted;                 // DEFAULT 0 -- orders attributed to this campaign

    @Column(name = "revenue_attributed", precision = 14, scale = 2)
    private BigDecimal revenueAttributed;       // NUMERIC(14,2) DEFAULT 0

    @Column(name = "budget_allocated", precision = 12, scale = 2)
    private BigDecimal budgetAllocated;         // NUMERIC(12,2) DEFAULT 0

    @Column(name = "current_spend", precision = 12, scale = 2)
    private BigDecimal currentSpend;            // NUMERIC(12,2) DEFAULT 0

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
