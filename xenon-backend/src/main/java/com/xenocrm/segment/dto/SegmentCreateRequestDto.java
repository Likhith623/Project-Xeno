package com.xenocrm.segment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SegmentCreateRequestDto — DTO for creating a new segment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentCreateRequestDto {

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String queryDsl;

    @NotBlank
    private String compiledSql;

    private String[] tags;

    private Boolean isDynamic;
}
