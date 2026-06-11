package com.xenocrm.customer.enums;

/**
 * PreferredChannel — Enum representing preferred communication channels.
 * Layer: Domain Enum
 * Purpose: Matches the 'preferred_channel' check constraint in the database.
 */
public enum PreferredChannel {
    EMAIL,
    WHATSAPP,
    SMS,
    RCS
}
