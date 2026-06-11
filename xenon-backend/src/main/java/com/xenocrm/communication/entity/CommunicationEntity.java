package com.xenocrm.communication.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.communication.enums.CommunicationStatus;
import com.xenocrm.customer.entity.CustomerEntity;
import com.xenocrm.order.entity.OrderEntity;
import com.xenocrm.variant.entity.MessageVariantEntity;
import com.xenocrm.variant.enums.MessageChannel;
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
@Table(name = "communications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class CommunicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private CampaignEntity campaign;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "variant_id", nullable = false)
    private MessageVariantEntity variant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.Type(io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType.class)
    @Column(name = "channel", columnDefinition = "message_channel")
    private MessageChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommunicationStatus status;

    @Column(name = "channel_message_id")
    private String channelMessageId;

    @Column(name = "recipient_address", nullable = false)
    private String recipientAddress;

    @Column(name = "personalised_subject")
    private String personalisedSubject;

    @Column(name = "personalised_body", columnDefinition = "TEXT")
    private String personalisedBody;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @Column(name = "delivered_at")
    private OffsetDateTime deliveredAt;

    @Column(name = "failed_at")
    private OffsetDateTime failedAt;

    @Column(name = "opened_at")
    private OffsetDateTime openedAt;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "clicked_at")
    private OffsetDateTime clickedAt;

    @Column(name = "converted_at")
    private OffsetDateTime convertedAt;

    @Column(name = "unsubscribed_at")
    private OffsetDateTime unsubscribedAt;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "failure_code")
    private String failureCode;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "next_retry_at")
    private OffsetDateTime nextRetryAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attributed_order_id")
    private OrderEntity attributedOrder;

    @Column(name = "attribution_window_hours")
    private int attributionWindowHours; // DEFAULT 72

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spawned_followup_id")
    private CommunicationEntity spawnedFollowup;

    @Column(name = "mab_sample_value", precision = 8, scale = 6)
    private BigDecimal mabSampleValue;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
