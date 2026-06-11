package com.xenocrm.customer.enums;

/**
 * CustomerGender -- Domain enum for gender IN (MALE,feMALE,OTHER,UNKNOWN).
 * Layer: Domain Enum
 * Converter: CustomerGenderConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum CustomerGender {
    MALE, FEMALE, OTHER, UNKNOWN
}
