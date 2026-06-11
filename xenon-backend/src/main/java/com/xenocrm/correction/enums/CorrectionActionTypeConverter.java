package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CorrectionActionTypeConverter -- JPA AttributeConverter that persists CorrectionActionType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CorrectionActionTypeConverter implements AttributeConverter<CorrectionActionType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CorrectionActionType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CorrectionActionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionActionType.valueOf(dbData.toUpperCase());
    }
}
