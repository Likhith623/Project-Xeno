package com.xenocrm.campaign.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CampaignStatusConverter -- JPA AttributeConverter that persists CampaignStatus as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CampaignStatusConverter implements AttributeConverter<CampaignStatus, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CampaignStatus attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CampaignStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CampaignStatus.valueOf(dbData.toUpperCase());
    }
}
