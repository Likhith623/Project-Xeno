package com.xenocrm.memory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemoryLearningTypeConverter implements AttributeConverter<MemoryLearningType, String> {

    @Override
    public String convertToDatabaseColumn(MemoryLearningType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public MemoryLearningType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : MemoryLearningType.valueOf(dbData.toUpperCase());
    }
}
