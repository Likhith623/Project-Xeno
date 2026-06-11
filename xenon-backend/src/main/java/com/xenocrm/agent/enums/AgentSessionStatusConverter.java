package com.xenocrm.agent.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AgentSessionStatusConverter implements AttributeConverter<AgentSessionStatus, String> {

    @Override
    public String convertToDatabaseColumn(AgentSessionStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public AgentSessionStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AgentSessionStatus.valueOf(dbData.toUpperCase());
    }
}
