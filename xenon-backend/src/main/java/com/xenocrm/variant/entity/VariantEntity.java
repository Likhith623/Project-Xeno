package com.xenocrm.variant.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * VariantEntity — JPA entity mapping to the `campaign_variants` table.
 */
@Entity
@Table(name = "campaign_variants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private CampaignEntity campaign;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "content_template", nullable = false, columnDefinition = "text")
    private String contentTemplate;

    @Column(name = "language", nullable = false)
    private String language; // DEFAULT 'en'

    @Column(name = "alpha", nullable = false)
    private Double alpha; // DEFAULT 1.0 - Thompson Sampling alpha (successes)

    @Column(name = "beta", nullable = false)
    private Double beta; // DEFAULT 1.0 - Thompson Sampling beta (failures)

    @Column(name = "is_control", nullable = false)
    private boolean isControl; // DEFAULT false

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
