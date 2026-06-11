package com.xenocrm.segment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SegmentTypeConverter implements AttributeConverter<SegmentType, String> {

    @Override
    public String convertToDatabaseColumn(SegmentType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public SegmentType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SegmentType.valueOf(dbData.toUpperCase());
    }
}
