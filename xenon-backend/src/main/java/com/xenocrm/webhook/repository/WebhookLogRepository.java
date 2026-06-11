package com.xenocrm.webhook.repository;

import com.xenocrm.webhook.entity.WebhookLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WebhookLogRepository extends JpaRepository<WebhookLogEntity, UUID> {
    List<WebhookLogEntity> findByWebhookIdOrderByCreatedAtDesc(UUID webhookId);
}
