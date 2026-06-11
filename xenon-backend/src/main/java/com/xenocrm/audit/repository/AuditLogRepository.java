package com.xenocrm.audit.repository;

import com.xenocrm.audit.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

/**
 * AuditLogRepository -- JPA repository for AuditLogEntity.
 * Layer: Repository
 */
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {
    /** Finds all audit logs for a specific entity type and ID, paginated. */
    Page<AuditLogEntity> findAllByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);
    /** Finds all audit logs belonging to a request trace ID, paginated. */
    Page<AuditLogEntity> findAllByTraceId(UUID traceId, Pageable pageable);
    /** Finds all audit logs by actor (API key hash, agent session ID, etc.), paginated. */
    Page<AuditLogEntity> findAllByActorId(String actorId, Pageable pageable);
}
