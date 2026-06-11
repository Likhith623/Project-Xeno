package com.xenocrm.channelservice.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CallbackProcessingStatusConverter -- JPA AttributeConverter that persists CallbackProcessingStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CallbackProcessingStatusConverter implements AttributeConverter<CallbackProcessingStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CallbackProcessingStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CallbackProcessingStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CallbackProcessingStatus.valueOf(dbData.toUpperCase());
    }
}
