package com.xenocrm.channelservice.enums;

/**
 * MessageChannel -- Domain enum for Postgres ENUM message_channel.
 * Layer: Domain Enum
 * Converter: MessageChannelConverter translates to/from lowercase for PostgreSQL CHECK constraints.
 */
public enum MessageChannel {
    EMAIL, WHATSAPP, SMS, RCS
}
