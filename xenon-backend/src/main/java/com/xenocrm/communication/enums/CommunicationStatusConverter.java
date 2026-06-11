package com.xenocrm.communication.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CommunicationStatusConverter implements AttributeConverter<CommunicationStatus, String> {

    @Override
    public String convertToDatabaseColumn(CommunicationStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CommunicationStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CommunicationStatus.valueOf(dbData.toUpperCase());
    }
}
