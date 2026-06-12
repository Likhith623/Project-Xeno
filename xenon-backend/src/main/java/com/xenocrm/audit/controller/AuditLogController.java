package com.xenocrm.audit.controller;

import com.xenocrm.audit.dto.AuditLogResponseDto;
import com.xenocrm.audit.service.AuditLogService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * AuditLogController — API endpoints for viewing audit logs.
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Audit Logs", description = "Endpoints for viewing system audit trails")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get audit logs by entity type and ID")
    public ResponseEntity<ResponseWrapper<Page<AuditLogResponseDto>>> getLogsForEntity(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @PageableDefault(size = 20) Pageable pageable) {
        Long parsedEntityId = 0L;
        try {
            parsedEntityId = Long.parseLong(entityId);
        } catch (NumberFormatException e) {
            // It's a UUID, so entityId is 0L
        }
        return ResponseEntity.ok(auditLogService.getLogsForEntity(entityType, parsedEntityId, pageable));
    }

    @GetMapping("/trace/{traceId}")
    @Operation(summary = "Get audit logs by trace ID")
    public ResponseEntity<ResponseWrapper<Page<AuditLogResponseDto>>> getLogsByTraceId(
            @PathVariable UUID traceId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getLogsByTraceId(traceId, pageable));
    }

    @GetMapping("/actor/{actorId}")
    @Operation(summary = "Get audit logs by actor ID")
    public ResponseEntity<ResponseWrapper<Page<AuditLogResponseDto>>> getLogsByActor(
            @PathVariable String actorId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getLogsByActor(actorId, pageable));
    }
}
