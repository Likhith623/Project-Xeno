package com.xenocrm.audit.service;

import com.xenocrm.audit.dto.AuditLogResponseDto;
import com.xenocrm.audit.entity.AuditLogEntity;
import com.xenocrm.audit.enums.AuditActorType;
import com.xenocrm.audit.mapper.AuditLogMapper;
import com.xenocrm.audit.repository.AuditLogRepository;
import com.xenocrm.common.PaginationMetadata;
import com.xenocrm.common.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLogService — Service for managing audit logs.
 */
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    /**
     * Logs an action with optional trace ID, UUID entity handling, and old/new values.
     * Note: UUIDs should be stored in the description field, and entityId should be set to 0L.
     *
     * @param traceId nullable trace ID
     * @param entityType type of entity being audited
     * @param entityId BIGINT entity ID, or 0L if the entity uses UUID
     * @param action string representation of the action performed
     * @param actorType USER | AGENT | SYSTEM
     * @param actorId API key hash, session ID, or scheduler ID
     * @param oldValue previous state (nullable)
     * @param newValue new state (nullable)
     * @param description human-readable summary (store UUID here if applicable)
     */
    @Transactional
    public void logAction(UUID traceId, String entityType, Long entityId, String action,
                          AuditActorType actorType, String actorId,
                          Map<String, Object> oldValue, Map<String, Object> newValue,
                          String description) {
        
        AuditLogEntity logEntry = AuditLogEntity.builder()
                .traceId(traceId)
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .actorType(actorType)
                .actorId(actorId)
                .oldValue(oldValue)
                .newValue(newValue)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        
        auditLogRepository.save(logEntry);
    }

    /**
     * Retrieves audit logs for a specific entity type and ID.
     */
    public ResponseWrapper<Page<AuditLogResponseDto>> getLogsForEntity(String entityType, Long entityId, Pageable pageable) {
        Page<AuditLogEntity> logs = auditLogRepository.findAllByEntityTypeAndEntityId(entityType, entityId, pageable);
        return ResponseWrapper.success(
                logs.map(auditLogMapper::toResponseDto),
                "Retrieved audit logs for entity",
                PaginationMetadata.from(logs)
        );
    }

    /**
     * Retrieves audit logs for a specific trace ID.
     */
    public ResponseWrapper<Page<AuditLogResponseDto>> getLogsByTraceId(UUID traceId, Pageable pageable) {
        Page<AuditLogEntity> logs = auditLogRepository.findAllByTraceId(traceId, pageable);
        return ResponseWrapper.success(
                logs.map(auditLogMapper::toResponseDto),
                "Retrieved audit logs for trace ID",
                PaginationMetadata.from(logs)
        );
    }

    /**
     * Retrieves audit logs by actor.
     */
    public ResponseWrapper<Page<AuditLogResponseDto>> getLogsByActor(String actorId, Pageable pageable) {
        Page<AuditLogEntity> logs = auditLogRepository.findAllByActorId(actorId, pageable);
        return ResponseWrapper.success(
                logs.map(auditLogMapper::toResponseDto),
                "Retrieved audit logs for actor",
                PaginationMetadata.from(logs)
        );
    }
}
