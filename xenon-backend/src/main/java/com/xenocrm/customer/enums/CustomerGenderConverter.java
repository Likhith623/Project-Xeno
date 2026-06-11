package com.xenocrm.customer.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CustomerGenderConverter implements AttributeConverter<CustomerGender, String> {

    @Override
    public String convertToDatabaseColumn(CustomerGender attribute) {
        return attribute == null ? null : attribute.name().toLowerCase();
    }

    @Override
    public CustomerGender convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CustomerGender.valueOf(dbData.toUpperCase());
    }
}
