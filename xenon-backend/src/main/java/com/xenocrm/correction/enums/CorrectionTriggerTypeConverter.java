package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CorrectionTriggerTypeConverter -- JPA AttributeConverter that persists CorrectionTriggerType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CorrectionTriggerTypeConverter implements AttributeConverter<CorrectionTriggerType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CorrectionTriggerType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CorrectionTriggerType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionTriggerType.valueOf(dbData.toUpperCase());
    }
}
