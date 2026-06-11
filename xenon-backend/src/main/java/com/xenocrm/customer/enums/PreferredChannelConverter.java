package com.xenocrm.customer.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PreferredChannelConverter implements AttributeConverter<PreferredChannel, String> {

    @Override
    public String convertToDatabaseColumn(PreferredChannel attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public PreferredChannel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : PreferredChannel.valueOf(dbData.toUpperCase());
    }
}
