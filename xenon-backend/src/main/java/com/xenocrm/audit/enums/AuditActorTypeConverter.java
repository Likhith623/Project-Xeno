package com.xenocrm.audit.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * AuditActorTypeConverter -- JPA AttributeConverter that persists AuditActorType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class AuditActorTypeConverter implements AttributeConverter<AuditActorType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(AuditActorType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public AuditActorType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AuditActorType.valueOf(dbData.toUpperCase());
    }
}
