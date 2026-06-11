package com.xenocrm.channelservice.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * ChannelCallbackEventTypeConverter -- JPA AttributeConverter that persists ChannelCallbackEventType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class ChannelCallbackEventTypeConverter implements AttributeConverter<ChannelCallbackEventType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(ChannelCallbackEventType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public ChannelCallbackEventType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ChannelCallbackEventType.valueOf(dbData.toUpperCase());
    }
}
