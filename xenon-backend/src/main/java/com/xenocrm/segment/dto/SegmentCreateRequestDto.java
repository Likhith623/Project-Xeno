package com.xenocrm.segment.dto;

import com.xenocrm.segment.enums.SegmentType;
import com.xenocrm.segment.enums.SegmentStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentCreateRequestDto {

    @NotBlank
    private String name;

    private String description;

    private SegmentType type;

    private SegmentStatus status;

    private String filterSql;

    private Map<String, Object> filterJson;

    private Boolean isPinned;

    private Boolean createdByAgent;

    private String agentGoal;
}
