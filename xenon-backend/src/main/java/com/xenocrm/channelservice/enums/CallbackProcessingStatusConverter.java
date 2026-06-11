package com.xenocrm.channelservice.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CallbackProcessingStatusConverter implements AttributeConverter<CallbackProcessingStatus, String> {

    @Override
    public String convertToDatabaseColumn(CallbackProcessingStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CallbackProcessingStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CallbackProcessingStatus.valueOf(dbData.toUpperCase());
    }
}
