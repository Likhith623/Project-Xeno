package com.xenocrm.variant.entity;

import com.xenocrm.campaign.entity.CampaignEntity;
import com.xenocrm.channelservice.enums.MessageChannel;
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
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "message_variants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class MessageVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private CampaignEntity campaign;

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.Type(io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType.class)
    @Column(name = "channel", columnDefinition = "message_channel", nullable = false)
    private MessageChannel channel;

    @Column(name = "copy_text", nullable = false)
    private String copyText;

    @Column(name = "template_id")
    private String templateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_params", columnDefinition = "jsonb")
    private Map<String, Object> templateParams;

    @Column(name = "is_winner")
    private boolean isWinner;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
