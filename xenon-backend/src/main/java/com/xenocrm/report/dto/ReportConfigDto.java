package com.xenocrm.report.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * ReportConfigDto — DTO representing a report configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportConfigDto {
    private UUID id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotBlank
    private String queryDsl;
    
    private Map<String, Object> visualizations;
    
    private Boolean isActive;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;
}
