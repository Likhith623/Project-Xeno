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
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CampaignStatus status;

    @Column(columnDefinition = "TEXT")
    private String goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id")
    private AudienceSegmentEntity targetSegment;

    @Column(name = "scheduled_at")
    private OffsetDateTime scheduledAt;

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "max_send_count")
    private Integer maxSendCount;

    @Column(name = "opt_out_rate_threshold", precision = 5, scale = 4)
    private BigDecimal optOutRateThreshold;

    @Column(name = "created_by_agent")
    private boolean createdByAgent;

    @Column(name = "agent_session_id")
    private String agentSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_campaign_id")
    private CampaignEntity parentCampaign;

    @Column(name = "total_sent")
    private int totalSent;

    @Column(name = "total_delivered")
    private int totalDelivered;

    @Column(name = "total_failed")
    private int totalFailed;

    @Column(name = "total_opened")
    private int totalOpened;

    @Column(name = "total_read")
    private int totalRead;

    @Column(name = "total_clicked")
    private int totalClicked;

    @Column(name = "total_converted")
    private int totalConverted;

    @Column(name = "revenue_attributed", precision = 14, scale = 2)
    private BigDecimal revenueAttributed;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
