package com.xenocrm.agent.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * AgentSessionStatusConverter -- JPA AttributeConverter that persists AgentSessionStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class AgentSessionStatusConverter implements AttributeConverter<AgentSessionStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(AgentSessionStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public AgentSessionStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AgentSessionStatus.valueOf(dbData.toUpperCase());
    }
}
