package com.xenocrm.communication.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.variant.entity.VariantEntity;
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
 * CommunicationLogEntity — JPA entity mapping to the `communication_logs` table.
 */
@Entity
@Table(name = "communication_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommunicationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private CampaignEntity campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private VariantEntity variant;

    @Column(name = "channel", nullable = false)
    private String channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommunicationStatus status;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @Column(name = "delivered_at")
    private OffsetDateTime deliveredAt;

    @Column(name = "opened_at")
    private OffsetDateTime openedAt;

    @Column(name = "clicked_at")
    private OffsetDateTime clickedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
