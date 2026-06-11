package com.xenocrm.audit.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AuditActorTypeConverter implements AttributeConverter<AuditActorType, String> {

    @Override
    public String convertToDatabaseColumn(AuditActorType attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public AuditActorType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AuditActorType.valueOf(dbData.toUpperCase());
    }
}
