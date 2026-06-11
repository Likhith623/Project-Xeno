package com.xenocrm.segment.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * SegmentStatusConverter -- JPA AttributeConverter that persists SegmentStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class SegmentStatusConverter implements AttributeConverter<SegmentStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(SegmentStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public SegmentStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SegmentStatus.valueOf(dbData.toUpperCase());
    }
}
