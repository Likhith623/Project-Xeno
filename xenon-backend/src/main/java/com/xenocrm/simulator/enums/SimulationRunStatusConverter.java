package com.xenocrm.simulator.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SimulationRunStatusConverter implements AttributeConverter<SimulationRunStatus, String> {

    @Override
    public String convertToDatabaseColumn(SimulationRunStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public SimulationRunStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SimulationRunStatus.valueOf(dbData.toUpperCase());
    }
}
