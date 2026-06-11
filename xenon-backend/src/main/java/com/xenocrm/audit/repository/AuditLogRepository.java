package com.xenocrm.audit.repository;

import com.xenocrm.audit.entity.AuditLogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * AuditLogRepository — Repository for the `audit_logs` table.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {

    /** Finds audit logs for a specific entity. */
    Page<AuditLogEntity> findAllByEntityTypeAndEntityId(String entityType, Long entityId, Pageable pageable);

    /** Finds audit logs for a specific trace ID. */
    Page<AuditLogEntity> findAllByTraceId(UUID traceId, Pageable pageable);

    /** Finds audit logs performed by a specific actor. */
    Page<AuditLogEntity> findAllByActorId(String actorId, Pageable pageable);
}
