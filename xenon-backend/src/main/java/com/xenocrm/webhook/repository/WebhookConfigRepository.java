package com.xenocrm.webhook.repository;

import com.xenocrm.webhook.entity.WebhookConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WebhookConfigRepository extends JpaRepository<WebhookConfigEntity, UUID> {
    List<WebhookConfigEntity> findByEventTypeAndIsActiveTrue(String eventType);
}
