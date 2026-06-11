package com.xenocrm.segment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SegmentStatusConverter implements AttributeConverter<SegmentStatus, String> {

    @Override
    public String convertToDatabaseColumn(SegmentStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public SegmentStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SegmentStatus.valueOf(dbData.toUpperCase());
    }
}
