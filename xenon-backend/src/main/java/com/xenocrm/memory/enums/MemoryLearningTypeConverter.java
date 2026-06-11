package com.xenocrm.memory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * MemoryLearningTypeConverter -- JPA AttributeConverter that persists MemoryLearningType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class MemoryLearningTypeConverter implements AttributeConverter<MemoryLearningType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(MemoryLearningType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public MemoryLearningType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MemoryLearningType.valueOf(dbData.toUpperCase());
    }
}
