package com.xenocrm.audit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLogDto — DTO representing an audit log entry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {
    private UUID id;
    
    @NotBlank
    private String entityName;
    
    @NotNull
    private UUID entityId;
    
    @NotBlank
    private String action;
    
    private UUID performedBy;
    
    private Map<String, Object> changes;
    
    private OffsetDateTime createdAt;
}
