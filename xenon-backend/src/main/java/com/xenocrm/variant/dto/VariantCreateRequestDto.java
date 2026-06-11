package com.xenocrm.variant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * VariantCreateRequestDto — DTO for creating a new campaign variant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantCreateRequestDto {

    @NotNull
    private UUID campaignId;

    @NotBlank
    private String name;

    @NotBlank
    private String contentTemplate;

    private String language; // Default en

    private Boolean isControl; // Default false
}
