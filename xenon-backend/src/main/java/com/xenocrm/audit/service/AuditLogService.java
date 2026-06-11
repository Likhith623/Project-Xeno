package com.xenocrm.audit.service;

import com.xenocrm.audit.dto.AuditLogDto;
import com.xenocrm.audit.entity.AuditLogEntity;
import com.xenocrm.audit.mapper.AuditLogMapper;
import com.xenocrm.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * AuditLogService — Handles creation and retrieval of audit logs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Transactional
    public AuditLogDto logAction(AuditLogDto request) {
        log.debug("Logging action: {} on entity: {} with id: {}", request.getAction(), request.getEntityName(), request.getEntityId());

        AuditLogEntity entity = auditLogMapper.toEntity(request);
        AuditLogEntity savedEntity = auditLogRepository.save(entity);
        
        return auditLogMapper.toDto(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<AuditLogDto> getAuditLogsForEntity(String entityName, UUID entityId) {
        return auditLogRepository.findByEntityNameAndEntityIdOrderByCreatedAtDesc(entityName, entityId).stream()
                .map(auditLogMapper::toDto)
                .collect(Collectors.toList());
    }
}
