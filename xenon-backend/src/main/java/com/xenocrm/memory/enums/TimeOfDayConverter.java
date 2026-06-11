package com.xenocrm.memory.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TimeOfDayConverter implements AttributeConverter<TimeOfDay, String> {

    @Override
    public String convertToDatabaseColumn(TimeOfDay attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public TimeOfDay convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TimeOfDay.valueOf(dbData.toUpperCase());
    }
}
