package com.xenocrm.campaign.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * CampaignMetricsEntity — JPA entity mapping to the `campaign_metrics` table.
 */
@Entity
@Table(name = "campaign_metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CampaignMetricsEntity {

    @Id
    @Column(name = "campaign_id")
    private UUID campaignId;

    @Column(name = "total_targeted")
    private Integer totalTargeted;

    @Column(name = "total_sent")
    private Integer totalSent;

    @Column(name = "total_delivered")
    private Integer totalDelivered;

    @Column(name = "total_failed")
    private Integer totalFailed;

    @Column(name = "total_opened")
    private Integer totalOpened;

    @Column(name = "total_clicked")
    private Integer totalClicked;

    @Column(name = "total_opt_outs")
    private Integer totalOptOuts;

    @Column(name = "total_conversions")
    private Integer totalConversions;

    @Column(name = "revenue_generated", precision = 12, scale = 2)
    private BigDecimal revenueGenerated;

    @LastModifiedDate
    @Column(name = "last_updated_at", nullable = false)
    private OffsetDateTime lastUpdatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaign;
}
