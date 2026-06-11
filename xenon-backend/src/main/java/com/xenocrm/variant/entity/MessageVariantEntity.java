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

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private CampaignEntity campaign;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @org.hibernate.annotations.Type(io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType.class)
    @Column(name = "channel", columnDefinition = "message_channel")
    private MessageChannel channel;

    @Column(name = "subject_line")
    private String subjectLine;

    @Column(name = "preview_text")
    private String previewText;

    @Column(name = "body_text", columnDefinition = "TEXT")
    private String bodyText;

    @Column(name = "body_html", columnDefinition = "TEXT")
    private String bodyHtml;

    @Column(name = "cta_text")
    private String ctaText;

    @Column(name = "cta_url")
    private String ctaUrl;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "template_id")
    private String templateId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_params", columnDefinition = "jsonb")
    private Map<String, Object> templateParams;

    @Column(name = "mab_alpha", precision = 10, scale = 4)
    private BigDecimal mabAlpha;

    @Column(name = "mab_beta", precision = 10, scale = 4)
    private BigDecimal mabBeta;

    @Column(name = "mab_impressions")
    private int mabImpressions;

    @Column(name = "mab_conversions")
    private int mabConversions;

    @Column(name = "mab_is_active")
    private boolean mabIsActive;

    @Column(name = "generated_by_ai")
    private boolean generatedByAi;

    @Column(name = "generation_prompt")
    private String generationPrompt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
