package com.xenocrm.audit.dto;

import com.xenocrm.audit.enums.AuditActorType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * AuditLogResponseDto — DTO for responding with audit log details.
 */
@Data
public class AuditLogResponseDto {
    private Long id;
    private UUID traceId;
    private String entityType;
    private Long entityId;
    private String action;
    private AuditActorType actorType;
    private String actorId;
    private Map<String, Object> oldValue;
    private Map<String, Object> newValue;
    private String description;
    private LocalDateTime createdAt;
}
