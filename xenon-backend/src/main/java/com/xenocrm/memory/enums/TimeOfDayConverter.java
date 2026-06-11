package com.xenocrm.memory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * TimeOfDayConverter -- JPA AttributeConverter that persists TimeOfDay as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class TimeOfDayConverter implements AttributeConverter<TimeOfDay, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(TimeOfDay attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public TimeOfDay convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TimeOfDay.valueOf(dbData.toUpperCase());
    }
}
