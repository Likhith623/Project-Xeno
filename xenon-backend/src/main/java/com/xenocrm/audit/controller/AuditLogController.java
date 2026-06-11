package com.xenocrm.audit.controller;

import com.xenocrm.audit.dto.AuditLogDto;
import com.xenocrm.audit.service.AuditLogService;
import com.xenocrm.common.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * AuditLogController — Exposes audit logging endpoints.
 */
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "Audit trail and logging endpoints")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @PostMapping
    @Operation(summary = "Log an action manually")
    public ResponseEntity<ResponseWrapper<AuditLogDto>> logAction(@Valid @RequestBody AuditLogDto request) {
        AuditLogDto responseDto = auditLogService.logAction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.success(responseDto));
    }

    @GetMapping("/{entityName}/{entityId}")
    @Operation(summary = "Get audit logs for a specific entity")
    public ResponseEntity<ResponseWrapper<List<AuditLogDto>>> getAuditLogs(
            @PathVariable String entityName, 
            @PathVariable UUID entityId) {
        List<AuditLogDto> responseDtos = auditLogService.getAuditLogsForEntity(entityName, entityId);
        return ResponseEntity.ok(ResponseWrapper.success(responseDtos));
    }
}
