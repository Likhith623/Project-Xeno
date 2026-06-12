package com.xenocrm.segment.dto;

import lombok.Data;

@Data
public class SegmentUpdateRequestDto {
    private String name;
    private String description;
    private String filterSql;
    private java.util.Map<String, Object> filterJson;
}
