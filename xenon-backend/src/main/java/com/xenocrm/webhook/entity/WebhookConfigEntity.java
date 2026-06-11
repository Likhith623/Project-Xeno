package com.xenocrm.webhook.entity;

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
 * WebhookConfigEntity — JPA entity mapping to the `webhook_configs` table.
 */
@Entity
@Table(name = "webhook_configs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WebhookConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "secret")
    private String secret;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
