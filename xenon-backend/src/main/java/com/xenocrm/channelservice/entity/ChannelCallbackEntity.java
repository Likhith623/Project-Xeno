package com.xenocrm.channelservice.entity;

import com.xenocrm.channelservice.enums.CallbackProcessingStatus;
import com.xenocrm.channelservice.enums.ChannelCallbackEventType;
import com.xenocrm.communication.entity.CommunicationEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * ChannelCallbackEntity — JPA entity mapping to the `channel_callbacks` table.
 * Layer: Domain Entity
 * Purpose: Raw webhook events from the stubbed channel service.
 * Relationships: ManyToOne with CommunicationEntity
 */
@Entity
@Table(name = "channel_callbacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChannelCallbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "communication_id")
    private CommunicationEntity communication; // nullable — resolved by Postgres trigger

    @Column(name = "channel_message_id")
    private String channelMessageId; // Used to match communication if FK is null

    
    
    @Column(name = "event_type", nullable = false)
    private ChannelCallbackEventType eventType; // NOT NULL

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> payload; // Raw callback body

    @CreatedDate
    @Column(name = "received_at", nullable = false, updatable = false)
    private OffsetDateTime receivedAt; // NOT NULL DEFAULT NOW()

    @Column(name = "processed_at")
    private OffsetDateTime processedAt; // Set by Postgres trigger fn_apply_callback

    
    
    @Column(name = "processing_status")
    private CallbackProcessingStatus processingStatus; // DEFAULT 'pending'

    @Column(name = "processing_error")
    private String processingError; // Set by trigger on error — nullable
}
