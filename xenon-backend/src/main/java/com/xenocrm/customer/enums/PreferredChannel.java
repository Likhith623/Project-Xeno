package com.xenocrm.customer.enums;

/**
 * PreferredChannel -- Domain enum for preferred_channel IN (EMAIL,WHATSAPP,SMS,RCS).
 * Layer: Domain Enum
 * Converter: PreferredChannelConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum PreferredChannel {
    EMAIL, WHATSAPP, SMS, RCS
}
