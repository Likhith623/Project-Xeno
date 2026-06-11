package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorrectionTriggerTypeConverter implements AttributeConverter<CorrectionTriggerType, String> {

    @Override
    public String convertToDatabaseColumn(CorrectionTriggerType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CorrectionTriggerType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionTriggerType.valueOf(dbData.toUpperCase());
    }
}
