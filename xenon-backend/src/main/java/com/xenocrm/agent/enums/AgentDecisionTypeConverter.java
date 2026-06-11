package com.xenocrm.agent.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AgentDecisionTypeConverter implements AttributeConverter<AgentDecisionType, String> {

    @Override
    public String convertToDatabaseColumn(AgentDecisionType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public AgentDecisionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AgentDecisionType.valueOf(dbData.toUpperCase());
    }
}
