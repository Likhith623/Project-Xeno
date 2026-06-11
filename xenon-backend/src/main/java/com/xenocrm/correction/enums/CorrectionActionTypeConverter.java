package com.xenocrm.correction.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CorrectionActionTypeConverter implements AttributeConverter<CorrectionActionType, String> {

    @Override
    public String convertToDatabaseColumn(CorrectionActionType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CorrectionActionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CorrectionActionType.valueOf(dbData.toUpperCase());
    }
}
