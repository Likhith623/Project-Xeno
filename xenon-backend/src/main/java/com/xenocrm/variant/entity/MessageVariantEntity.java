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

/**
 * MessageVariantEntity -- JPA entity mapping to the `message_variants` table.
 * Layer: Domain Entity
 * Purpose: One A/B variant (copy + channel) for a campaign; MAB drives selection.
 */
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
    private CampaignEntity campaign;            // FK to campaigns.id NOT NULL ON DELETE CASCADE

    @Column(name = "name", nullable = false)
    private String name;                        // NOT NULL -- e.g. Email - urgency copy A

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "channel", columnDefinition = "message_channel", nullable = false)
    private MessageChannel channel;             // Postgres ENUM -- must include columnDefinition

    @Column(name = "subject_line")
    private String subjectLine;                 // email subject -- nullable

    @Column(name = "preview_text")
    private String previewText;                 // email preview -- nullable

    @Column(name = "body_text", columnDefinition = "TEXT")
    private String bodyText;                    // plaintext/WhatsApp body -- nullable

    @Column(name = "body_html", columnDefinition = "TEXT")
    private String bodyHtml;                    // rich HTML email -- nullable

    @Column(name = "cta_text")
    private String ctaText;                     // CTA button label -- nullable

    @Column(name = "cta_url")
    private String ctaUrl;                      // destination URL -- nullable

    @Column(name = "media_url")
    private String mediaUrl;                    // image/video URL -- nullable

    @Column(name = "template_id")
    private String templateId;                  // WA Business template ID -- nullable

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "template_params", columnDefinition = "jsonb")
    private Map<String, Object> templateParams; // template placeholder values

    @Column(name = "mab_alpha", precision = 10, scale = 4)
    private BigDecimal mabAlpha;                // NUMERIC(10,4) DEFAULT 1.0 -- successes + prior 1

    @Column(name = "mab_beta", precision = 10, scale = 4)
    private BigDecimal mabBeta;                 // NUMERIC(10,4) DEFAULT 1.0 -- failures + prior 1

    @Column(name = "mab_impressions")
    private int mabImpressions;                 // DEFAULT 0 -- total deliveries recorded

    @Column(name = "mab_conversions")
    private int mabConversions;                 // DEFAULT 0 -- total clicks/conversions recorded

    @Column(name = "mab_is_active")
    private boolean mabIsActive;                // DEFAULT TRUE -- set false to disable this variant

    @Column(name = "generated_by_ai")
    private boolean generatedByAi;              // DEFAULT FALSE

    @Column(name = "generation_prompt", columnDefinition = "TEXT")
    private String generationPrompt;            // nullable -- prompt used to generate this variant

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
