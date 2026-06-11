package com.xenocrm.ml.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * ModelTrainingLogDto — DTO representing a model training log.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelTrainingLogDto {
    private UUID id;
    
    @NotBlank
    private String modelName;
    
    @NotBlank
    private String modelVersion;
    
    private String status;
    
    private Map<String, Object> metrics;
    
    private OffsetDateTime startedAt;
    
    private OffsetDateTime completedAt;
    
    private OffsetDateTime createdAt;
}
