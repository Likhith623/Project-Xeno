package com.xenocrm.channelservice.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ChannelCallbackEventTypeConverter implements AttributeConverter<ChannelCallbackEventType, String> {

    @Override
    public String convertToDatabaseColumn(ChannelCallbackEventType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public ChannelCallbackEventType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ChannelCallbackEventType.valueOf(dbData.toUpperCase());
    }
}
