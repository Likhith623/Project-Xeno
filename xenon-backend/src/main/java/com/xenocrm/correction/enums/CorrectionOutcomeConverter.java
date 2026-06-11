package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CorrectionOutcomeConverter -- JPA AttributeConverter that persists CorrectionOutcome as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CorrectionOutcomeConverter implements AttributeConverter<CorrectionOutcome, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CorrectionOutcome attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CorrectionOutcome convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionOutcome.valueOf(dbData.toUpperCase());
    }
}
