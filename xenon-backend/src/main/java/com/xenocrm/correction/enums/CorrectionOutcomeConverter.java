package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorrectionOutcomeConverter implements AttributeConverter<CorrectionOutcome, String> {

    @Override
    public String convertToDatabaseColumn(CorrectionOutcome attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CorrectionOutcome convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionOutcome.valueOf(dbData.toUpperCase());
    }
}
