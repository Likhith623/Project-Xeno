package com.xenocrm.settings.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * SystemSettingsDto — DTO representing a system setting.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemSettingsDto {
    private UUID id;
    
    @NotBlank
    private String key;
    
    @NotBlank
    private String value;
    
    private String description;
    
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
