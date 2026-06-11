package com.xenocrm.customer.enums;

/**
 * CustomerGender -- Domain enum for gender IN (male,female,other,unknown).
 * Layer: Domain Enum
 * Converter: CustomerGenderConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CustomerGender {
    MALE, FEMALE, OTHER, UNKNOWN
}
