package com.xenocrm.customer.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * CustomerGenderConverter -- JPA AttributeConverter that persists CustomerGender as lowercase
 * to satisfy PostgreSQL CHECK constraints, while Java code uses UPPERCASE enum values.
 * Layer: JPA Converter
 */
@Converter(autoApply = true)
public class CustomerGenderConverter implements AttributeConverter<CustomerGender, String> {

    /** Converts Java enum (UPPERCASE) to DB string (lowercase). */
    @Override
    public String convertToDatabaseColumn(CustomerGender attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    /** Converts DB string (lowercase) to Java enum (UPPERCASE). */
    @Override
    public CustomerGender convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CustomerGender.valueOf(dbData.toUpperCase());
    }
}
