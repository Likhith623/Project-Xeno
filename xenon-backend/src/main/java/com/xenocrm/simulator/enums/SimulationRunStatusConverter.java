package com.xenocrm.simulator.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * SimulationRunStatusConverter -- JPA AttributeConverter that persists SimulationRunStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class SimulationRunStatusConverter implements AttributeConverter<SimulationRunStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(SimulationRunStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public SimulationRunStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : SimulationRunStatus.valueOf(dbData.toUpperCase());
    }
}
