package com.xenocrm.segment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * SegmentTypeConverter -- JPA AttributeConverter that persists SegmentType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class SegmentTypeConverter implements AttributeConverter<SegmentType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(SegmentType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public SegmentType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SegmentType.valueOf(dbData.toUpperCase());
    }
}
