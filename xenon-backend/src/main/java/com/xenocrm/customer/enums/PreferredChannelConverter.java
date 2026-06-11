package com.xenocrm.customer.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * PreferredChannelConverter -- JPA AttributeConverter that persists PreferredChannel as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class PreferredChannelConverter implements AttributeConverter<PreferredChannel, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(PreferredChannel attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public PreferredChannel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : PreferredChannel.valueOf(dbData.toUpperCase());
    }
}
