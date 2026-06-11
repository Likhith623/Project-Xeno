package com.xenocrm.webhook.service;

import com.xenocrm.exception.ResourceNotFoundException;
import com.xenocrm.webhook.dto.WebhookConfigDto;
import com.xenocrm.webhook.dto.WebhookLogDto;
import com.xenocrm.webhook.entity.WebhookConfigEntity;
import com.xenocrm.webhook.entity.WebhookLogEntity;
import com.xenocrm.webhook.enums.WebhookStatus;
import com.xenocrm.webhook.mapper.WebhookMapper;
import com.xenocrm.webhook.repository.WebhookConfigRepository;
import com.xenocrm.webhook.repository.WebhookLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * WebhookService — Handles webhook configurations and async executions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final WebhookConfigRepository webhookConfigRepository;
    private final WebhookLogRepository webhookLogRepository;
    private final WebhookMapper webhookMapper;

    @Transactional
    public WebhookConfigDto createConfig(WebhookConfigDto request) {
        log.debug("Creating webhook config for URL: {}", request.getUrl());

        WebhookConfigEntity entity = webhookMapper.toConfigEntity(request);
        
        if (request.getIsActive() != null) {
            entity.setActive(request.getIsActive());
        } else {
            entity.setActive(true);
        }
        
        if (request.getRetryCount() != null) {
            entity.setRetryCount(request.getRetryCount());
        } else {
            entity.setRetryCount(3);
        }

        WebhookConfigEntity savedEntity = webhookConfigRepository.save(entity);
        return webhookMapper.toConfigDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<WebhookConfigDto> getAllConfigs() {
        return webhookConfigRepository.findAll().stream()
                .map(webhookMapper::toConfigDto)
                .collect(Collectors.toList());
    }

    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> triggerWebhooksAsync(String eventType, Map<String, Object> payload) {
        log.debug("Triggering webhooks for event type: {}", eventType);

        return CompletableFuture.supplyAsync(() -> {
            List<WebhookConfigEntity> configs = webhookConfigRepository.findByEventTypeAndIsActiveTrue(eventType);
            
            for (WebhookConfigEntity config : configs) {
                // Simulate HTTP call
                log.debug("Sending payload to webhook URL: {}", config.getUrl());
                
                WebhookLogEntity logEntity = new WebhookLogEntity();
                logEntity.setWebhook(config);
                logEntity.setPayload(payload);
                
                // Simulate success/failure
                boolean isSuccess = Math.random() > 0.1; // 90% success
                
                if (isSuccess) {
                    logEntity.setStatus(WebhookStatus.SUCCESS);
                    logEntity.setResponseCode(200);
                    logEntity.setResponseBody("{\"status\":\"ok\"}");
                } else {
                    logEntity.setStatus(WebhookStatus.FAILED);
                    logEntity.setResponseCode(500);
                    logEntity.setResponseBody("{\"error\":\"Internal Server Error\"}");
                }
                
                webhookLogRepository.save(logEntity);
            }
            return null;
        });
    }

    @Transactional(readOnly = true)
    public List<WebhookLogDto> getLogsByWebhookId(UUID webhookId) {
        return webhookLogRepository.findByWebhookIdOrderByCreatedAtDesc(webhookId).stream()
                .map(webhookMapper::toLogDto)
                .collect(Collectors.toList());
    }
}
