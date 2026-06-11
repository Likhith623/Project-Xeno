package com.xenocrm.agent.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * AgentDecisionTypeConverter -- JPA AttributeConverter that persists AgentDecisionType as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class AgentDecisionTypeConverter implements AttributeConverter<AgentDecisionType, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(AgentDecisionType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public AgentDecisionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AgentDecisionType.valueOf(dbData.toUpperCase());
    }
}
