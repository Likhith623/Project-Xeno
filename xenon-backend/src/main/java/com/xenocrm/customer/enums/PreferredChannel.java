package com.xenocrm.customer.enums;

/**
 * PreferredChannel -- Domain enum for preferred_channel IN (email,whatsapp,sms,rcs).
 * Layer: Domain Enum
 * Converter: PreferredChannelConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum PreferredChannel {
    EMAIL, WHATSAPP, SMS, RCS
}
