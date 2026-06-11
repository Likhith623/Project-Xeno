package com.xenocrm.communication.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CommunicationStatusConverter -- JPA AttributeConverter that persists CommunicationStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CommunicationStatusConverter implements AttributeConverter<CommunicationStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CommunicationStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CommunicationStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CommunicationStatus.valueOf(dbData.toUpperCase());
    }
}
